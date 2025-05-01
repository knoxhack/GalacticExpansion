/**
 * Integration script for the build widget to handle GitHub releases
 * Enhanced with version tracking and changelog support
 */

const { exec } = require('child_process');
const path = require('path');
const fs = require('fs');

// Version constants
const VERSION_BASE = '0.1.0';
const BUILD_COUNTER_FILE = '.build_counter';

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
        
        console.log('Creating GitHub release...');
        
        // Export version as environment variable for the release script
        const env = { ...process.env, GALACTIC_VERSION: version };
        
        // Then create a GitHub release with the version
        const releaseResult = await runCommand('bash scripts/push_to_github_release.sh', env);
        
        return {
            success: releaseResult.success,
            message: releaseResult.success ? 'Build and release successful!' : 'Release failed!',
            output: releaseResult.output,
            version: version
        };
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

module.exports = { buildAndRelease };