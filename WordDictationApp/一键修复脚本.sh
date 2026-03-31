#!/bin/bash

echo "========================================"
echo "词语听写小助手 - Gradle Wrapper修复脚本"
echo "========================================"

echo ""
echo "步骤1: 下载gradle-wrapper.jar"
echo ""

# 创建目录
mkdir -p gradle/wrapper

# 尝试从多个源下载gradle-wrapper.jar
echo "正在从官方源下载gradle-wrapper.jar..."
wget -q -O gradle/wrapper/gradle-wrapper.jar \
  https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar

if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "下载成功！"
else
    echo "下载失败，尝试备用方案..."
    echo "请手动下载gradle-wrapper.jar并放入 gradle/wrapper/ 目录"
    echo "下载地址: https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar"
    exit 1
fi

echo ""
echo "步骤2: 设置执行权限"
echo ""

chmod +x gradlew

echo ""
echo "步骤3: 测试Gradle Wrapper"
echo ""

./gradlew --version

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "Gradle Wrapper修复成功！"
    echo "========================================"
    echo ""
    echo "现在可以编译项目："
    echo "./gradlew assembleDebug"
    echo ""
    echo "APK文件将生成在："
    echo "app/build/outputs/apk/debug/app-debug.apk"
else
    echo ""
    echo "========================================"
    echo "Gradle Wrapper测试失败"
    echo "========================================"
    echo ""
    echo "可能的原因："
    echo "1. 缺少Java JDK"
    echo "2. JAVA_HOME环境变量未设置"
    echo "3. 网络问题导致下载失败"
    echo ""
    echo "解决方案："
    echo "1. 安装JDK 11或更高版本"
    echo "2. 设置JAVA_HOME环境变量"
    echo "3. 手动下载gradle-wrapper.jar"
fi

echo ""