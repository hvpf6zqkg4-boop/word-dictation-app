@echo off
REM 词语听写小助手 - 编译脚本
REM 适用于 Windows

echo ========================================
echo 词语听写小助手 - APK 编译脚本
echo ========================================

REM 检查是否在项目目录
if not exist "settings.gradle" (
    echo 错误：请在项目根目录运行此脚本
    pause
    exit /b 1
)

REM 检查 Gradle 包装器
if not exist "gradlew.bat" (
    echo 警告：未找到 gradlew.bat 文件
    echo 正在尝试修复...
    call 一键修复脚本.bat
    if %errorlevel% neq 0 (
        echo 修复失败，请手动运行 一键修复脚本.bat
        pause
        exit /b 1
    )
)

echo 1. 清理项目...
call gradlew clean

if %errorlevel% neq 0 (
    echo 清理失败
    pause
    exit /b 1
)

echo 2. 编译调试版 APK...
call gradlew assembleDebug

if %errorlevel% neq 0 (
    echo 编译失败
    pause
    exit /b 1
)

REM 检查 APK 文件
set APK_PATH=app\build\outputs\apk\debug\app-debug.apk
if exist "%APK_PATH%" (
    for /f %%i in ('powershell -command "(Get-Item '%APK_PATH%').length/1KB"') do set APK_SIZE=%%i
    echo ========================================
    echo 编译成功！
    echo APK 文件: %APK_PATH%
    echo 文件大小: %APK_SIZE% KB
    echo ========================================
    
    REM 显示安装命令
    echo 安装命令:
    echo   adb install %APK_PATH%
    echo.
    echo 或者手动复制到手机安装
) else (
    echo 错误：未找到 APK 文件
    pause
    exit /b 1
)

REM 可选：编译发布版
echo.
set /p compile_release=是否编译发布版？(y/n): 
if /i "%compile_release%"=="y" (
    echo 3. 编译发布版 APK...
    call gradlew assembleRelease
    
    if %errorlevel% neq 0 (
        echo 发布版编译失败
        pause
        exit /b 1
    )
    
    set RELEASE_APK=app\build\outputs\apk\release\app-release-unsigned.apk
    if exist "%RELEASE_APK%" (
        for /f %%i in ('powershell -command "(Get-Item '%RELEASE_APK%').length/1KB"') do set RELEASE_SIZE=%%i
        echo ========================================
        echo 发布版编译成功！
        echo APK 文件: %RELEASE_APK%
        echo 文件大小: %RELEASE_SIZE% KB
        echo 注意：发布版需要签名才能安装
        echo ========================================
    )
)

echo.
echo 编译完成！
pause