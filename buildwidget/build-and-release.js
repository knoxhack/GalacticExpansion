/**
 * Integration script for the build widget to handle GitHub releases
 */

const { exec } = require('child_process');
const path = require('path');

/**
 * Build and release the project to GitHub
 * @returns {Promise<object>} Build result
 */
async function buildAndRelease() {
    console.log('Starting build and release process...');
    
    try {
        // First, build the project
        console.log('Building project...');
        const buildResult = await runCommand('./gradlew clean build --no-daemon');
        
        if (!buildResult.success) {
            console.error('Build failed!');
            return { success: false, message: 'Build failed', output: buildResult.output };
        }
        
        console.log('Build successful! Creating GitHub release...');
        
        // Then create a GitHub release
        const releaseResult = await runCommand('bash scripts/push_to_github_release.sh');
        
        return {
            success: releaseResult.success,
            message: releaseResult.success ? 'Build and release successful!' : 'Release failed!',
            output: releaseResult.output
        };
    } catch (error) {
        console.error('Error in build and release process:', error);
        return { success: false, message: 'Process error', output: error.toString() };
    }
}

/**
 * Run a shell command
 * @param {string} command Command to execute
 * @returns {Promise<object>} Command result
 */
function runCommand(command) {
    return new Promise((resolve) => {
        console.log(`Executing: ${command}`);
        
        const process = exec(command, { maxBuffer: 10 * 1024 * 1024 }); // 10MB buffer
        
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