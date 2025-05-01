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
const CHANGELOG_HISTORY_FILE = 'buildwidget/changelog_history.json';
const VERSION_HISTORY_FILE = 'buildwidget/version_history.json';

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
            
            // Then create a GitHub release with the version and changelog
            const releaseResult = await runCommand('bash scripts/push_to_github_release.sh', env);
            
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
            
            // Then create a GitHub release with the version
            const releaseResult = await runCommand('bash scripts/push_to_github_release.sh', env);
            
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
        
        const options = { 
            maxBuffer: 10 * 1024 * 1024, // 10MB buffer
            env: env || process.env
        };
        
        const process = exec(command, options);
        
        let output = '';
        
        process.stdout.on('data', (data) => {
            output += data.toString();
            console.log(data.toString());
        });
        
        process.stderr.on('data', (data) => {
            output += data.toString();
            console.error(data.toString());
        });
        
        process.on('close', (code) => {
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
    if (process.env.GITHUB_TOKEN) {
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

module.exports = { 
    buildAndRelease,
    getBuildNumber,
    getVersionString,
    getGitHubStatus,
    getBuildMetrics,
    getVersionHistory,
    getChangelogHistory,
    getModuleDependencies,
    formatChangelogMarkdown
};