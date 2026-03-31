#!/bin/bash

# 词语听写小助手 - 编译脚本
# 适用于 Linux/macOS

echo "========================================"
echo "词语听写小助手 - APK 编译脚本"
echo "========================================"

# 检查是否在项目目录
if [ ! -f "settings.gradle" ]; then
    echo "错误：请在项目根目录运行此脚本"
    exit 1
fi

# 检查 Gradle 包装器
if [ ! -f "gradlew" ]; then
    echo "警告：未找到 gradlew 文件"
    echo "正在尝试修复..."
    chmod +x 一键修复脚本.sh
    ./一键修复脚本.sh
    if [ $? -ne 0 ]; then
        echo "修复失败，请手动运行 ./一键修复脚本.sh"
        exit 1
    fi
fi

# 赋予执行权限
chmod +x gradlew

echo "1. 清理项目..."
./gradlew clean

if [ $? -ne 0 ]; then
    echo "清理失败"
    exit 1
fi

echo "2. 编译调试版 APK..."
./gradlew assembleDebug

if [ $? -ne 0 ]; then
    echo "编译失败"
    exit 1
fi

# 检查 APK 文件
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
    APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
    echo "========================================"
    echo "编译成功！"
    echo "APK 文件: $APK_PATH"
    echo "文件大小: $APK_SIZE"
    echo "========================================"
    
    # 显示安装命令
    echo "安装命令:"
    echo "  adb install $APK_PATH"
    echo ""
    echo "或者手动复制到手机安装"
else
    echo "错误：未找到 APK 文件"
    exit 1
fi

# 可选：编译发布版
echo ""
read -p "是否编译发布版？(y/n): " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "3. 编译发布版 APK..."
    ./gradlew assembleRelease
    
    if [ $? -ne 0 ]; then
        echo "发布版编译失败"
        exit 1
    fi
    
    RELEASE_APK="app/build/outputs/apk/release/app-release-unsigned.apk"
    if [ -f "$RELEASE_APK" ]; then
        RELEASE_SIZE=$(du -h "$RELEASE_APK" | cut -f1)
        echo "========================================"
        echo "发布版编译成功！"
        echo "APK 文件: $RELEASE_APK"
        echo "文件大小: $RELEASE_SIZE"
        echo "注意：发布版需要签名才能安装"
        echo "========================================"
    fi
fi

echo ""
echo "编译完成！"