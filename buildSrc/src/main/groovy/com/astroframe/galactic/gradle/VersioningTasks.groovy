package com.astroframe.galactic.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.options.Option

/**
 * Plugin that adds versioning-related tasks to the project.
 */
class VersioningPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.tasks.register('bumpMajorVersion', BumpVersionTask) {
            versionType = 'major'
            description = 'Bumps the major version (x.0.0)'
            group = 'versioning'
        }

        project.tasks.register('bumpMinorVersion', BumpVersionTask) {
            versionType = 'minor'
            description = 'Bumps the minor version (0.x.0)'
            group = 'versioning'
        }

        project.tasks.register('bumpPatchVersion', BumpVersionTask) {
            versionType = 'patch'
            description = 'Bumps the patch version (0.0.x)'
            group = 'versioning'
        }
        
        project.tasks.register('showCurrentVersion', CurrentVersionTask) {
            description = 'Shows the current mod version'
            group = 'versioning'
        }
        
        project.tasks.register('setVersion', SetVersionTask) {
            description = 'Sets a specific version'
            group = 'versioning'
        }
    }
}

/**
 * Task to bump the version number.
 */
class BumpVersionTask extends DefaultTask {
    String versionType
    
    @TaskAction
    void bumpVersion() {
        File propertiesFile = new File(project.rootDir, 'gradle.properties')
        Properties properties = new Properties()
        
        // Load existing properties
        propertiesFile.withInputStream { properties.load(it) }
        
        // Get current version
        String currentVersion = properties.getProperty('mod_version')
        if (!currentVersion) {
            throw new IllegalStateException("mod_version property not found in gradle.properties")
        }
        
        // Split version into components
        String[] versionParts = currentVersion.split('\\.')
        if (versionParts.length != 3) {
            throw new IllegalStateException("Version format incorrect. Expected 'x.y.z', found '$currentVersion'")
        }
        
        int major = Integer.parseInt(versionParts[0])
        int minor = Integer.parseInt(versionParts[1])
        int patch = Integer.parseInt(versionParts[2])
        
        String newVersion
        switch (versionType) {
            case 'major':
                newVersion = "${major + 1}.0.0"
                break
            case 'minor':
                newVersion = "${major}.${minor + 1}.0"
                break
            case 'patch':
                newVersion = "${major}.${minor}.${patch + 1}"
                break
            default:
                throw new IllegalArgumentException("Unknown version type: ${versionType}")
        }
        
        // Update property
        properties.setProperty('mod_version', newVersion)
        
        // Save properties file
        propertiesFile.withOutputStream { properties.store(it, "Updated by version bumping task") }
        
        logger.lifecycle("Version updated from ${currentVersion} to ${newVersion}")
        
        // Update any other version-related files
        updateChangelogHeader(newVersion)
    }
    
    private void updateChangelogHeader(String newVersion) {
        File changelogFile = new File(project.rootDir, 'CHANGELOG.md')
        if (!changelogFile.exists()) {
            logger.lifecycle("CHANGELOG.md not found, skipping changelog update")
            return
        }
        
        String content = changelogFile.text
        String versionType = this.versionType.capitalize()
        String newEntry = """
## [$newVersion] - $versionType Release - ${new Date().format('yyyy-MM-dd')}

### Added
- 

### Changed
- 

### Fixed
- 

"""
        
        // Add new version entry after the header
        int headerEndIndex = content.indexOf("\n## ")
        if (headerEndIndex == -1) {
            // No existing entries, add after the introduction
            changelogFile.text = content.trim() + "\n\n" + newEntry.trim() + "\n"
        } else {
            // Insert before the first entry
            changelogFile.text = content.substring(0, headerEndIndex) + "\n" + newEntry + content.substring(headerEndIndex)
        }
        
        logger.lifecycle("Updated CHANGELOG.md with new version entry")
    }
}

/**
 * Task to show the current version.
 */
class CurrentVersionTask extends DefaultTask {
    @TaskAction
    void showVersion() {
        File propertiesFile = new File(project.rootDir, 'gradle.properties')
        Properties properties = new Properties()
        
        // Load existing properties
        propertiesFile.withInputStream { properties.load(it) }
        
        // Get current version
        String currentVersion = properties.getProperty('mod_version')
        if (!currentVersion) {
            throw new IllegalStateException("mod_version property not found in gradle.properties")
        }
        
        logger.lifecycle("Current version: ${currentVersion}")
    }
}

/**
 * Task to set a specific version.
 */
class SetVersionTask extends DefaultTask {
    @Option(option = 'new-version', description = 'The new version to set (format: x.y.z)')
    String newVersion = null
    
    @TaskAction
    void setVersion() {
        if (newVersion == null) {
            throw new IllegalArgumentException("New version must be specified with --new-version=x.y.z")
        }
        
        // Validate version format
        if (!newVersion.matches("\\d+\\.\\d+\\.\\d+")) {
            throw new IllegalArgumentException("Invalid version format. Expected 'x.y.z', got '$newVersion'")
        }
        
        File propertiesFile = new File(project.rootDir, 'gradle.properties')
        Properties properties = new Properties()
        
        // Load existing properties
        propertiesFile.withInputStream { properties.load(it) }
        
        // Get current version
        String currentVersion = properties.getProperty('mod_version')
        if (!currentVersion) {
            throw new IllegalStateException("mod_version property not found in gradle.properties")
        }
        
        // Update property
        properties.setProperty('mod_version', newVersion)
        
        // Save properties file
        propertiesFile.withOutputStream { properties.store(it, "Updated by setVersion task") }
        
        logger.lifecycle("Version updated from ${currentVersion} to ${newVersion}")
        
        // Update any other version-related files
        updateChangelogHeader(newVersion)
    }
    
    private void updateChangelogHeader(String newVersion) {
        File changelogFile = new File(project.rootDir, 'CHANGELOG.md')
        if (!changelogFile.exists()) {
            logger.lifecycle("CHANGELOG.md not found, skipping changelog update")
            return
        }
        
        String content = changelogFile.text
        String newEntry = """
## [$newVersion] - Custom Release - ${new Date().format('yyyy-MM-dd')}

### Added
- 

### Changed
- 

### Fixed
- 

"""
        
        // Add new version entry after the header
        int headerEndIndex = content.indexOf("\n## ")
        if (headerEndIndex == -1) {
            // No existing entries, add after the introduction
            changelogFile.text = content.trim() + "\n\n" + newEntry.trim() + "\n"
        } else {
            // Insert before the first entry
            changelogFile.text = content.substring(0, headerEndIndex) + "\n" + newEntry + content.substring(headerEndIndex)
        }
        
        logger.lifecycle("Updated CHANGELOG.md with new version entry")
    }
}