#!/bin/bash
# 简化版编译脚本 - 使用最少依赖

echo "正在创建简化版项目..."

# 创建简化版build.gradle
cat > app/build.gradle << 'EOF'
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.example.worddictation'
    compileSdk 33

    defaultConfig {
        applicationId "com.example.worddictation"
        minSdk 24
        targetSdk 33
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
        }
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.10.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // 使用系统TTS
    // 使用系统相机（无需额外依赖）
}
EOF

echo "简化版项目创建完成！"
echo "现在可以尝试编译："
echo "./gradlew assembleDebug"