#!/usr/bin/env node

/**
 * ==========================================================
 *  Adam Signal - Automated APK Builder Script
 *  Developed by Adam Dev
 * ==========================================================
 *  Usage:
 *    node build-apk.js
 *    OR
 *    npm run build:apk
 * ==========================================================
 */

const { spawn } = require('child_process');
const fs = require('fs');
const path = require('path');

const COLORS = {
  reset: '\x1b[0m',
  cyan: '\x1b[36m',
  green: '\x1b[32m',
  yellow: '\x1b[33m',
  red: '\x1b[31m',
  bold: '\x1b[1m'
};

function log(msg, color = COLORS.reset) {
  console.log(`${color}${msg}${COLORS.reset}`);
}

function printBanner() {
  log('====================================================', COLORS.cyan);
  log('        ADAM SIGNAL - APK BUILD ENGINE             ', COLORS.bold + COLORS.cyan);
  log('             Developed by Adam Dev                 ', COLORS.green);
  log('====================================================', COLORS.cyan);
  log('');
}

function resolveGradleCommand() {
  const isWin = process.platform === 'win32';
  const gradlew = path.join(process.cwd(), isWin ? 'gradlew.bat' : 'gradlew');

  if (fs.existsSync(gradlew)) {
    return { cmd: isWin ? gradlew : './gradlew', args: [':app:assembleDebug'] };
  }

  // Fallback to system gradle
  return { cmd: isWin ? 'gradle.bat' : 'gradle', args: [':app:assembleDebug'] };
}

function buildApk() {
  printBanner();

  const { cmd, args } = resolveGradleCommand();
  log(`🚀 Starting Gradle build: ${cmd} ${args.join(' ')}...`, COLORS.yellow);

  const startTime = Date.now();
  const buildProcess = spawn(cmd, args, {
    stdio: 'inherit',
    shell: true,
    cwd: process.cwd()
  });

  buildProcess.on('error', (err) => {
    log(`\n❌ Failed to launch build command: ${err.message}`, COLORS.red);
    process.exit(1);
  });

  buildProcess.on('close', (code) => {
    const elapsed = ((Date.now() - startTime) / 1000).toFixed(1);

    if (code !== 0) {
      log(`\n❌ Gradle build failed with exit code: ${code} (took ${elapsed}s)`, COLORS.red);
      process.exit(code);
    }

    log(`\n✨ Build succeeded in ${elapsed}s!`, COLORS.green);

    // Locate generated APK
    const candidatePaths = [
      path.join(process.cwd(), 'app', 'build', 'outputs', 'apk', 'debug', 'app-debug.apk'),
      path.join(process.cwd(), 'app', 'build', 'outputs', 'apk', 'release', 'app-release-unsigned.apk')
    ];

    let sourceApk = candidatePaths.find(p => fs.existsSync(p));

    if (!sourceApk) {
      log('⚠️ Could not find compiled APK in expected output directories.', COLORS.yellow);
      process.exit(1);
    }

    // Destination at project root
    const targetApk = path.join(process.cwd(), 'adam-signal.apk');

    try {
      fs.copyFileSync(sourceApk, targetApk);
      const stats = fs.statSync(targetApk);
      const sizeMB = (stats.size / (1024 * 1024)).toFixed(2);

      log('');
      log('====================================================', COLORS.green);
      log('🎉 APK READY AT PROJECT ROOT:', COLORS.bold + COLORS.green);
      log(`   📁 File: adam-signal.apk`, COLORS.cyan);
      log(`   📦 Size: ${sizeMB} MB`, COLORS.cyan);
      log(`   📍 Path: ${targetApk}`, COLORS.cyan);
      log('   🛡️ Engineered by Adam Dev', COLORS.yellow);
      log('====================================================', COLORS.green);
      log('');
    } catch (copyErr) {
      log(`❌ Error copying APK to root: ${copyErr.message}`, COLORS.red);
      process.exit(1);
    }
  });
}

buildApk();
