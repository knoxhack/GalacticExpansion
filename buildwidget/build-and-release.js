/**
 * Integration script for the build widget to handle GitHub releases
 * Enhanced with version tracking, changelog generation and GitHub integration
 */

const { exec } = require('child_process');
const path = require('path');
const fs = require('fs');

// Version constants
const VERSION_BASE = '0.1.0';
const BUILD_COUNTER_FILE = '.build_counter';
const CHANGELOG_HISTORY_FILE = path.join(__dirname, 'changelog_history.json');
const VERSION_HISTORY_FILE = path.join(__dirname, 'version_history.json');

// Create history files if they don't exist
try {
    // Ensure the history files can be created
    if (!fs.existsSync(VERSION_HISTORY_FILE)) {
        fs.writeFileSync(VERSION_HISTORY_FILE, '[]', 'utf8');
        console.log('Created empty version history file');
    }
    if (!fs.existsSync(CHANGELOG_HISTORY_FILE)) {
        fs.writeFileSync(CHANGELOG_HISTORY_FILE, '[]', 'utf8');
        console.log('Created empty changelog history file');
    }
} catch (error) {
    console.error('Error creating history files:', error);
}

// Change categories and their priorities for advanced changelog parsing
const CHANGE_CATEGORIES = {
    'BREAKING CHANGE': { priority: 0, emoji: '💥', label: 'Breaking Changes' },
    'feat': { priority: 1, emoji: '✨', label: 'New Features' },
    'fix': { priority: 2, emoji: '🐛', label: 'Bug Fixes' },
    'perf': { priority: 3, emoji: '⚡', label: 'Performance Improvements' },
    'refactor': { priority: 4, emoji: '♻️', label: 'Code Refactoring' },
    'docs': { priority: 5, emoji: '📝', label: 'Documentation' },
    'style': { priority: 6, emoji: '💄', label: 'Styles' },
    'test': { priority: 7, emoji: '✅', label: 'Tests' },
    'build': { priority: 8, emoji: '🛠️', label: 'Build System' },
    'ci': { priority: 9, emoji: '👷', label: 'CI' }
};

/**
 * Get the current build number
 * @returns {number} Current build number
 */
function getBuildNumber() {
    try {
        if (fs.existsSync(BUILD_COUNTER_FILE)) {
            const counter = parseInt(fs.readFileSync(BUILD_COUNTER_FILE, 'utf8').trim(), 10);
            return isNaN(counter) ? 1 : counter;
        }
    } catch (err) {
        console.error('Error reading build counter file:', err);
    }
    
    return 1; // Default to 1 if file doesn't exist or can't be read
}

/**
 * Increment the build counter
 */
function incrementBuildCounter() {
    const currentBuild = getBuildNumber();
    const nextBuild = currentBuild + 1;
    
    try {
        fs.writeFileSync(BUILD_COUNTER_FILE, nextBuild.toString(), 'utf8');
        console.log(`Build counter incremented to ${nextBuild}`);
    } catch (err) {
        console.error('Error updating build counter file:', err);
    }
    
    return nextBuild;
}

/**
 * Get the current version string
 * @returns {string} Version string in format 0.1.0.bX-YYYYMMDD
 */
function getVersionString() {
    const buildNumber = getBuildNumber();
    const date = new Date();
    const dateStr = date.getFullYear().toString() +
                    (date.getMonth() + 1).toString().padStart(2, '0') +
                    date.getDate().toString().padStart(2, '0');
    
    return `${VERSION_BASE}.b${buildNumber}-${dateStr}`;
}

/**
 * Generate a structured changelog for each module
 * @param {string} version The version string
 * @returns {Promise<object>} Generated changelog data
 */
async function generateModuleChangelogs(version) {
    console.log('Generating structured changelog...');
    
    // Get commits since last tag/release
    const commitsResult = await runCommand('git log --pretty=format:"%h||%s||%an||%ad" --date=short -n 100');
    
    if (!commitsResult.success) {
        console.error('Failed to get commit history');
        return null;
    }
    
    // Initialize changelog structure
    const changelog = {
        version: version,
        date: new Date().toISOString(),
        modules: {},
        categories: {},
        summary: [],
        breaking_changes: []
    };
    
    // Define modules to track (match naming in the repository)
    const moduleNames = ['core', 'machinery', 'biotech', 'power', 'construction', 
                         'energy', 'space', 'utilities', 'robotics', 'vehicles', 'weaponry'];
    
    // Initialize modules in changelog
    moduleNames.forEach(module => {
        changelog.modules[module] = [];
    });
    
    // Initialize categories
    Object.keys(CHANGE_CATEGORIES).forEach(category => {
        changelog.categories[category] = [];
    });
    
    // Process each commit
    const commits = commitsResult.output.split('\n').filter(Boolean);
    
    for (const commit of commits) {
        try {
            const [hash, message, author, date] = commit.split('||');
            
            // Skip merge commits
            if (message.startsWith('Merge ')) continue;
            
            // Skip Replit-specific metadata
            if (message.includes('Replit-Commit-')) continue;
            
            const commitEntry = { hash, message, author, date };
            
            // Check for breaking changes
            if (message.includes('BREAKING CHANGE')) {
                changelog.breaking_changes.push(commitEntry);
            }
            
            // Categorize by conventional commit type
            let categorized = false;
            for (const [category, info] of Object.entries(CHANGE_CATEGORIES)) {
                if (message.startsWith(`${category}:`)) {
                    const cleanMessage = message.substring(category.length + 1).trim();
                    changelog.categories[category].push({
                        ...commitEntry,
                        message: cleanMessage
                    });
                    categorized = true;
                    break;
                }
            }
            
            // If not categorized by type, add to general summary
            if (!categorized) {
                changelog.summary.push(commitEntry);
            }
            
            // Categorize by module
            let moduleCategorized = false;
            for (const module of moduleNames) {
                // Check various patterns that might indicate module changes
                const patterns = [
                    new RegExp(`\\[${module}\\]`, 'i'),
                    new RegExp(`^${module}:`, 'i'),
                    new RegExp(`in ${module} module`, 'i'),
                    new RegExp(`for ${module}`, 'i')
                ];
                
                if (patterns.some(pattern => pattern.test(message))) {
                    changelog.modules[module].push(commitEntry);
                    moduleCategorized = true;
                    break;
                }
            }
            
            // If not categorized by module, try to infer from file paths
            if (!moduleCategorized) {
                // Get affected files for this commit
                const filesResult = await runCommand(`git show --name-only --format='' ${hash}`);
                if (filesResult.success) {
                    const files = filesResult.output.split('\n').filter(Boolean);
                    
                    for (const module of moduleNames) {
                        if (files.some(file => file.includes(`/${module}/`) || file.startsWith(module + '/'))) {
                            changelog.modules[module].push(commitEntry);
                            break;
                        }
                    }
                }
            }
        } catch (error) {
            console.error('Error processing commit:', error);
        }
    }
    
    return changelog;
}

/**
 * Save changelog to history file
 * @param {object} changelog The changelog to save
 */
function saveChangelogHistory(changelog) {
    try {
        let history = [];
        
        // Read existing history if available
        if (fs.existsSync(CHANGELOG_HISTORY_FILE)) {
            try {
                history = JSON.parse(fs.readFileSync(CHANGELOG_HISTORY_FILE, 'utf8'));
            } catch (err) {
                console.error('Error parsing changelog history:', err);
            }
        }
        
        // Add this changelog to history (limit to last 10)
        history.unshift(changelog);
        if (history.length > 10) history = history.slice(0, 10);
        
        // Save updated history
        fs.writeFileSync(CHANGELOG_HISTORY_FILE, JSON.stringify(history, null, 2), 'utf8');
        console.log('Changelog history updated');
    } catch (error) {
        console.error('Failed to save changelog history:', error);
    }
}

/**
 * Save version information to history file
 * @param {string} version Version string
 * @param {object} metadata Additional metadata
 */
function saveVersionHistory(version, metadata = {}) {
    try {
        let history = [];
        
        // Read existing history if available
        if (fs.existsSync(VERSION_HISTORY_FILE)) {
            try {
                history = JSON.parse(fs.readFileSync(VERSION_HISTORY_FILE, 'utf8'));
            } catch (err) {
                console.error('Error parsing version history:', err);
            }
        }
        
        // Add this version to history
        const versionEntry = {
            version,
            date: new Date().toISOString(),
            buildNumber: getBuildNumber(),
            ...metadata
        };
        
        // Avoid duplicates
        const existingIndex = history.findIndex(entry => entry.version === version);
        if (existingIndex >= 0) {
            history[existingIndex] = versionEntry;
        } else {
            history.unshift(versionEntry);
        }
        
        // Limit history size
        if (history.length > 20) history = history.slice(0, 20);
        
        // Save updated history
        fs.writeFileSync(VERSION_HISTORY_FILE, JSON.stringify(history, null, 2), 'utf8');
        console.log('Version history updated');
    } catch (error) {
        console.error('Failed to save version history:', error);
    }
}

/**
 * Get a short summary of recent commit changes
 * @param {number} limit Maximum number of commits to include
 * @returns {Promise<Array>} Array of short commit summaries
 */
async function getShortCommitChanges(limit = 5) {
  console.log(`Getting short commit summaries (limit: ${limit})...`);
  
  try {
    // Get the most recent commits with short format
    const commitsResult = await runCommand(`git log --pretty=format:"%h||%s||%an||%ad" --date=short -n ${limit}`);
    
    if (!commitsResult.success) {
      console.error('Failed to get commit history for short summaries');
      return [];
    }
    
    // Process each commit into a simple format
    const commits = commitsResult.output.split('\n').filter(Boolean);
    const results = [];
    
    for (const commit of commits) {
      try {
        const [hash, message, author, date] = commit.split('||');
        
        // Skip merge commits and Replit metadata
        if (message.startsWith('Merge ') || message.includes('Replit-Commit-')) {
          continue;
        }
        
        // Format commit message
        const shortMessage = formatShortCommitMessage(message);
        
        results.push({
          hash: hash.substring(0, 7),  // Short hash
          message: shortMessage,
          author: author.split(' ')[0], // First name only
          date: date,
          raw: message
        });
        
        if (results.length >= limit) {
          break;
        }
      } catch (error) {
        console.error('Error processing commit for short summary:', error);
      }
    }
    
    return results;
  } catch (error) {
    console.error('Error getting short commit changes:', error);
    return [];
  }
}

/**
 * Format commit message into a shorter version
 * @param {string} message The original commit message
 * @returns {string} Shortened commit message
 */
function formatShortCommitMessage(message) {
  // Remove common prefixes from conventional commits
  const prefixes = ['feat:', 'fix:', 'chore:', 'docs:', 'style:', 'refactor:', 'perf:', 'test:', 'build:', 'ci:'];
  
  let shortMessage = message;
  for (const prefix of prefixes) {
    if (message.startsWith(prefix)) {
      shortMessage = message.substring(prefix.length).trim();
      break;
    }
  }
  
  // Remove scope if present
  shortMessage = shortMessage.replace(/^\([\w-]+\)\s*/, '');
  
  // Truncate to reasonable length (50 chars)
  if (shortMessage.length > 50) {
    shortMessage = shortMessage.substring(0, 47) + '...';
  }
  
  return shortMessage;
}

/**
 * Format the changelog into markdown for GitHub release
 * @param {object} changelog The changelog object
 * @returns {string} Formatted markdown
 */
function formatChangelogMarkdown(changelog) {
    let markdown = `# Release ${changelog.version}\n\n`;
    
    // Add breaking changes first if any
    if (changelog.breaking_changes && changelog.breaking_changes.length > 0) {
        markdown += `## 💥 Breaking Changes\n\n`;
        for (const change of changelog.breaking_changes) {
            markdown += `- ${change.message} (\`${change.hash}\`)\n`;
        }
        markdown += '\n';
    }
    
    // Add categorized changes
    for (const [category, info] of Object.entries(CHANGE_CATEGORIES)) {
        const changes = changelog.categories[category];
        if (changes && changes.length > 0) {
            markdown += `## ${info.emoji} ${info.label}\n\n`;
            for (const change of changes) {
                markdown += `- ${change.message} (\`${change.hash}\`)\n`;
            }
            markdown += '\n';
        }
    }
    
    // Add module-specific changes
    markdown += `## 📦 Module Changes\n\n`;
    for (const [module, changes] of Object.entries(changelog.modules)) {
        if (changes && changes.length > 0) {
            markdown += `### ${module.charAt(0).toUpperCase() + module.slice(1)}\n\n`;
            for (const change of changes) {
                markdown += `- ${change.message} (\`${change.hash}\`)\n`;
            }
            markdown += '\n';
        }
    }
    
    // Add summary of other changes
    if (changelog.summary && changelog.summary.length > 0) {
        markdown += `## 🔄 Other Changes\n\n`;
        for (const change of changelog.summary) {
            markdown += `- ${change.message} (\`${change.hash}\`)\n`;
        }
    }
    
    return markdown;
}

/**
 * Build and release the project to GitHub
 * @returns {Promise<object>} Build result
 */
async function buildAndRelease() {
    console.log('Starting build and release process...');
    
    try {
        // Get version information
        const version = getVersionString();
        console.log(`Using version: ${version}`);
        
        // First, build the project
        console.log('Building project...');
        const buildResult = await runCommand('./gradlew clean build --no-daemon');
        
        if (!buildResult.success) {
            console.error('Build failed!');
            return { success: false, message: 'Build failed', output: buildResult.output };
        }
        
        // Increment build counter after successful build
        const newBuildNumber = incrementBuildCounter();
        console.log(`Build successful! Next build will be #${newBuildNumber}`);
        
        // Generate changelog
        const changelog = await generateModuleChangelogs(version);
        
        if (changelog) {
            console.log('Changelog generated successfully');
            
            // Save changelog to history
            saveChangelogHistory(changelog);
            
            // Format changelog for GitHub release
            const changelogMarkdown = formatChangelogMarkdown(changelog);
            
            // Save changelog to a temp file for the release script
            const changelogFile = 'buildwidget/temp_changelog.md';
            fs.writeFileSync(changelogFile, changelogMarkdown, 'utf8');
            
            // Record build metrics
            const buildEndTime = new Date();
            const buildMetrics = {
                duration: buildEndTime - new Date(buildResult.startTime || Date.now() - 60000),
                result: 'success'
            };
            
            // Save version history with build metrics
            saveVersionHistory(version, {
                buildMetrics,
                changelogSummary: changelog.summary.length + 
                                  Object.values(changelog.categories).reduce((count, changes) => count + changes.length, 0),
                moduleChanges: Object.fromEntries(
                    Object.entries(changelog.modules).map(([module, changes]) => [module, changes.length])
                )
            });
            
            console.log('Creating GitHub release...');
            
            // Export version and changelog file as environment variables for the release script
            const env = { 
                ...process.env, 
                GALACTIC_VERSION: version,
                CHANGELOG_FILE: changelogFile
            };
            
            // Create a GitHub release with the version and changelog using the improved script
            console.log('Using improved GitHub release script...');
            const releaseResult = await runCommand('bash scripts/improved_github_release.sh', env);
            
            // Clean up temp file
            if (fs.existsSync(changelogFile)) {
                fs.unlinkSync(changelogFile);
            }
            
            return {
                success: releaseResult.success,
                message: releaseResult.success ? 'Build and release successful!' : 'Release failed!',
                output: releaseResult.output,
                version: version,
                changelog: changelog
            };
        } else {
            console.log('Creating GitHub release without enhanced changelog...');
            
            // Export version as environment variable for the release script
            const env = { ...process.env, GALACTIC_VERSION: version };
            
            // Create a GitHub release with the version using improved script
            const releaseResult = await runCommand('bash scripts/improved_github_release.sh', env);
            
            // Still save version history without changelog
            saveVersionHistory(version, { buildMetrics: { result: 'success' } });
            
            return {
                success: releaseResult.success,
                message: releaseResult.success ? 'Build and release successful!' : 'Release failed!',
                output: releaseResult.output,
                version: version
            };
        }
    } catch (error) {
        console.error('Error in build and release process:', error);
        return { success: false, message: 'Process error', output: error.toString() };
    }
}

/**
 * Run a shell command with optional environment variables
 * @param {string} command Command to execute
 * @param {object} [env] Optional environment variables
 * @returns {Promise<object>} Command result
 */
function runCommand(command, env = null) {
    return new Promise((resolve) => {
        console.log(`Executing: ${command}`);
        
        // Get Node.js global process safely
        const nodeProcess = global.process;
        
        const options = { 
            maxBuffer: 10 * 1024 * 1024, // 10MB buffer
            env: env || nodeProcess.env
        };
        
        const childProc = exec(command, options);
        
        let output = '';
        
        childProc.stdout.on('data', (data) => {
            output += data.toString();
            console.log(data.toString());
        });
        
        childProc.stderr.on('data', (data) => {
            output += data.toString();
            console.error(data.toString());
        });
        
        childProc.on('close', (code) => {
            console.log(`Command exited with code ${code}`);
            resolve({ success: code === 0, output });
        });
    });
}

/**
 * Get GitHub release information and PR status
 * @returns {Promise<object>} GitHub status
 */
async function getGitHubStatus() {
    console.log('Fetching GitHub status...');
    
    // Get latest release info
    const releaseResult = await runCommand('git fetch --tags && git describe --tags --abbrev=0 || echo "No releases yet"');
    
    // Get PR info if we have GitHub token
    let prStatus = null;
    if (global.process && global.process.env && global.process.env.GITHUB_TOKEN) {
        try {
            // This would need GitHub CLI or direct API calls - placeholder for now
            const prResult = await runCommand('echo "GitHub PR integration placeholder"');
            prStatus = { success: true, message: 'PRs fetched' };
        } catch (error) {
            console.error('Error fetching PR status:', error);
        }
    }
    
    return {
        latestRelease: releaseResult.success ? releaseResult.output.trim() : null,
        prStatus
    };
}

/**
 * Get build metrics with trend data
 * @returns {object} Build metrics
 */
function getBuildMetrics() {
    try {
        // Read version history for metrics
        if (fs.existsSync(VERSION_HISTORY_FILE)) {
            const history = JSON.parse(fs.readFileSync(VERSION_HISTORY_FILE, 'utf8'));
            
            // Calculate average build time for successful builds
            const successfulBuilds = history.filter(v => v.buildMetrics && v.buildMetrics.result === 'success');
            
            let avgDuration = 0;
            if (successfulBuilds.length > 0) {
                avgDuration = successfulBuilds.reduce((sum, build) => 
                    sum + (build.buildMetrics?.duration || 0), 0) / successfulBuilds.length;
            }
            
            // Calculate success rate
            const totalBuildsWithMetrics = history.filter(v => v.buildMetrics).length;
            const successRate = totalBuildsWithMetrics > 0 
                ? (successfulBuilds.length / totalBuildsWithMetrics) * 100 
                : 100;
            
            // Get module change frequency
            const moduleChanges = {};
            history.forEach(v => {
                if (v.moduleChanges) {
                    Object.entries(v.moduleChanges).forEach(([module, count]) => {
                        moduleChanges[module] = (moduleChanges[module] || 0) + count;
                    });
                }
            });
            
            return {
                builds: {
                    total: history.length,
                    successful: successfulBuilds.length,
                    successRate
                },
                performance: {
                    avgBuildTime: avgDuration,
                    trend: successfulBuilds.slice(0, 5).map(b => b.buildMetrics?.duration || 0)
                },
                moduleActivity: moduleChanges
            };
        }
    } catch (error) {
        console.error('Error getting build metrics:', error);
    }
    
    return { builds: { total: 0, successful: 0, successRate: 0 }, performance: { avgBuildTime: 0, trend: [] } };
}

/**
 * Get version history data
 */
function getVersionHistory() {
    try {
        if (fs.existsSync(VERSION_HISTORY_FILE)) {
            return JSON.parse(fs.readFileSync(VERSION_HISTORY_FILE, 'utf8'));
        }
    } catch (error) {
        console.error('Error reading version history:', error);
    }
    
    return [];
}

/**
 * Get changelog history data
 */
function getChangelogHistory() {
    try {
        if (fs.existsSync(CHANGELOG_HISTORY_FILE)) {
            return JSON.parse(fs.readFileSync(CHANGELOG_HISTORY_FILE, 'utf8'));
        }
    } catch (error) {
        console.error('Error reading changelog history:', error);
    }
    
    return [];
}

/**
 * Generate module dependency graph
 * @returns {object} Module dependencies
 */
function getModuleDependencies() {
    // This would ideally parse build.gradle files to extract real dependencies
    // For now, create a simplified representation based on logical dependencies
    return {
        core: [],
        power: ['core'],
        machinery: ['core', 'power'],
        biotech: ['core'],
        construction: ['core', 'machinery'],
        energy: ['core', 'power'],
        space: ['core', 'power', 'machinery'],
        utilities: ['core'],
        robotics: ['core', 'machinery', 'power'],
        vehicles: ['core', 'power', 'machinery'],
        weaponry: ['core', 'power']
    };
}

// Removed duplicate handleCheckpoint function

/**
 * Handle a checkpoint event
 * @param {string} checkpointName Name of the checkpoint
 * @param {string} description Description of the checkpoint
 * @returns {Promise<object>} Operation result
 */
async function handleCheckpoint(checkpointName, description) {
    try {
        console.log(`Creating checkpoint: ${checkpointName}`);
        
        // Set git config to use the correct user
        await runCommand('git config --local user.name "knoxhack"');
        await runCommand('git config --local user.email "knoxhack@gmail.com"');
        
        // Add all files
        await runCommand('git add -A');
        
        // Create a commit with the checkpoint name and description
        const commitMessage = `Checkpoint: ${checkpointName}\n\n${description || 'Automated checkpoint with build and release'}`;
        const commitResult = await runCommand(`git commit -m "${commitMessage}"`);
        
        console.log('Commit result:', commitResult);
        
        // Save checkpoint metadata
        const timestamp = new Date().toISOString();
        const buildNumber = getBuildNumber();
        const checkpointData = {
            name: checkpointName,
            description: description || 'Automated checkpoint with build and release',
            timestamp: timestamp,
            buildNumber: buildNumber
        };
        
        // Save as the last checkpoint
        saveLastCheckpoint(checkpointData);
        
        // Trigger a build
        const buildResult = await buildAndRelease();
        
        return {
            success: true,
            commitResult,
            buildResult,
            checkpointName,
            checkpointData,
            timestamp
        };
    } catch (error) {
        console.error('Error creating checkpoint:', error);
        return {
            success: false,
            error: error.message || 'Unknown error occurred'
        };
    }
}

/**
 * Save the last checkpoint information to a file
 * @param {object} checkpoint The checkpoint metadata to save
 */
function saveLastCheckpoint(checkpoint) {
    try {
        // Ensure directory exists
        const fs = require('fs');
        const path = require('path');
        
        // Try different paths for the data directory
        const possibleDataDirs = [
            path.resolve('./buildwidget/data'),
            path.resolve(__dirname, './data'),
            path.resolve(__dirname, 'data')
        ];
        
        // Use the first possible path or create it
        let dataDir;
        for (const dir of possibleDataDirs) {
            if (fs.existsSync(dir)) {
                dataDir = dir;
                break;
            }
        }
        
        // If no directory exists, create the first one
        if (!dataDir) {
            dataDir = possibleDataDirs[0];
            fs.mkdirSync(dataDir, { recursive: true });
            console.log('Created checkpoint data directory at:', dataDir);
        }
        
        const checkpointData = JSON.stringify(checkpoint, null, 2);
        const filePath = path.join(dataDir, 'last-checkpoint.json');
        fs.writeFileSync(filePath, checkpointData);
        console.log('Saved checkpoint data to:', filePath);
        console.log('Last checkpoint data saved');
    } catch (error) {
        console.error('Error saving last checkpoint data:', error);
    }
}

/**
 * Get the last checkpoint information
 * @returns {object|null} Last checkpoint data or null if none exists
 */
function getLastCheckpoint() {
    try {
        const fs = require('fs');
        const path = require('path');
        // Try multiple paths to find the file
        const possiblePaths = [
            path.resolve('./buildwidget/data/last-checkpoint.json'),
            path.resolve(__dirname, './data/last-checkpoint.json'),
            path.resolve(__dirname, 'data/last-checkpoint.json')
        ];
        
        // Find the first path that exists
        const filePath = possiblePaths.find(p => fs.existsSync(p));
        
        // If no path exists, return null
        if (!filePath) {
            console.log('Checkpoint file not found in any of the expected locations:', possiblePaths);
            return null;
        }
        
        const data = fs.readFileSync(filePath, 'utf8');
        return JSON.parse(data);
    } catch (error) {
        console.error('Error reading last checkpoint data:', error);
        return null;
    }
}

module.exports = { 
    buildAndRelease,
    getBuildNumber,
    getVersionString,
    getGitHubStatus,
    getBuildMetrics,
    handleCheckpoint,
    getVersionHistory,
    getChangelogHistory,
    getModuleDependencies,
    formatChangelogMarkdown,
    saveVersionHistory,
    saveChangelogHistory,
    getShortCommitChanges,
    formatShortCommitMessage,
    saveLastCheckpoint,
    getLastCheckpoint
};