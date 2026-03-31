# GitHub自动编译APK指南

通过以下步骤，你可以在GitHub上自动编译APK并直接下载：

## 步骤1：创建GitHub仓库

1. 访问 https://github.com
2. 点击右上角 "+" → "New repository"
3. 填写仓库信息：
   - Repository name: `word-dictation-app`
   - Description: `词语听写小助手 - Android应用`
   - 选择 Public（公开）
   - 勾选 "Add a README file"
4. 点击 "Create repository"

## 步骤2：上传项目代码

### 方法A：使用Git命令行
```bash
# 1. 克隆仓库
git clone https://github.com/你的用户名/word-dictation-app.git
cd word-dictation-app

# 2. 复制所有项目文件到仓库
cp -r /path/to/WordDictationApp/* .

# 3. 提交代码
git add .
git commit -m "初始提交：词语听写小助手完整项目"
git push origin main
```

### 方法B：使用GitHub网页上传
1. 在GitHub仓库页面，点击 "Add file" → "Upload files"
2. 将整个 `WordDictationApp` 文件夹拖到上传区域
3. 填写提交信息
4. 点击 "Commit changes"

## 步骤3：触发自动编译

### 自动触发（推荐）
1. 代码上传后，GitHub Actions会自动开始编译
2. 访问仓库的 "Actions" 标签页
3. 查看编译进度

### 手动触发
1. 访问仓库的 "Actions" 标签页
2. 选择 "Android Build" 工作流
3. 点击 "Run workflow"
4. 选择分支（main）
5. 点击 "Run workflow"

## 步骤4：下载APK文件

### 从Artifacts下载
1. 编译完成后，点击对应的运行记录
2. 在 "Artifacts" 部分找到 "word-dictation-app"
3. 点击下载APK文件

### 从Release下载（如果设置了）
1. 访问仓库的 "Releases" 标签页
2. 找到最新版本
3. 下载APK文件

## 步骤5：安装和使用

### 安装APK
1. 将下载的APK文件传输到Android手机
2. 在手机上打开文件管理器
3. 找到APK文件并点击安装
4. 如果提示"未知来源"，请先启用"允许安装未知来源应用"

### 首次使用
1. 打开应用
2. 授予必要的权限（相机、存储）
3. 首次使用TTS时，系统会提示下载中文语音包
4. 开始使用

## 自动编译配置说明

我已经为你配置了完整的GitHub Actions工作流：

### 工作流文件：`.github/workflows/android-build.yml`
- **触发条件**：代码推送、Pull Request、手动触发
- **运行环境**：Ubuntu最新版 + JDK 11 + Android SDK
- **编译命令**：`./gradlew assembleDebug`
- **输出产物**：APK文件作为Artifact
- **发布选项**：可自动创建GitHub Release

### 编译过程
1. ✅ 检出代码
2. ✅ 设置JDK 11环境
3. ✅ 设置Android SDK
4. ✅ 编译APK（调试版）
5. ✅ 上传APK作为Artifact
6. ✅ （可选）创建Release

## 常见问题解决

### Q1: 编译失败怎么办？
**可能原因**：
1. 依赖下载失败
2. Android SDK版本不兼容
3. 代码语法错误

**解决方法**：
1. 检查网络连接
2. 查看编译日志中的错误信息
3. 确保代码完整上传

### Q2: 如何修改编译配置？
编辑 `.github/workflows/android-build.yml`：
```yaml
# 修改Android SDK版本
- name: Setup Android SDK
  uses: android-actions/setup-android@v2
  with:
    api-level: 34
    build-tools: 34.0.0
    ndk: 25.2.9519653
    cmake: 3.22.1
```

### Q3: 如何编译发布版？
修改工作流文件，添加发布版编译：
```yaml
- name: Build Release APK
  run: ./gradlew assembleRelease
```

### Q4: APK文件在哪里？
- **调试版**：`app/build/outputs/apk/debug/app-debug.apk`
- **发布版**：`app/build/outputs/apk/release/app-release-unsigned.apk`

## 高级功能

### 1. 自动版本号
```yaml
- name: Set version from tag
  if: startsWith(github.ref, 'refs/tags/')
  run: |
    VERSION=${GITHUB_REF#refs/tags/}
    echo "VERSION_NAME=$VERSION" >> $GITHUB_ENV
```

### 2. 代码签名
```yaml
- name: Sign APK
  run: |
    echo "${{ secrets.KEYSTORE }}" | base64 -d > keystore.jks
    ./gradlew assembleRelease
```

### 3. 多版本编译
```yaml
strategy:
  matrix:
    abi: [armeabi-v7a, arm64-v8a, x86, x86_64]
```

## 监控和维护

### 监控编译状态
1. **GitHub Actions仪表板**：查看所有工作流运行状态
2. **邮件通知**：GitHub会自动发送编译状态邮件
3. **状态徽章**：在README中添加编译状态徽章

### 定期维护
1. **更新依赖**：定期更新Gradle依赖版本
2. **安全检查**：检查安全漏洞
3. **性能优化**：优化编译速度和APK大小

## 安全建议

### 保护敏感信息
1. **不要提交密钥**：将签名密钥存储在GitHub Secrets中
2. **使用环境变量**：敏感配置使用环境变量
3. **定期轮换密钥**：定期更新签名密钥

### 代码安全
1. **代码扫描**：启用GitHub代码扫描
2. **依赖检查**：检查依赖库的安全漏洞
3. **权限最小化**：仅请求必要权限

## 扩展功能

### 1. 自动化测试
```yaml
- name: Run tests
  run: ./gradlew test
```

### 2. 代码质量检查
```yaml
- name: Lint check
  run: ./gradlew lint
```

### 3. 发布到应用商店
```yaml
- name: Deploy to Google Play
  uses: r0adkll/upload-google-play@v1
  with:
    serviceAccountJson: ${{ secrets.GOOGLE_PLAY_SERVICE_ACCOUNT }}
    packageName: com.example.worddictation
    releaseFiles: app/build/outputs/apk/release/*.apk
```

## 获取帮助

### GitHub文档
- [GitHub Actions文档](https://docs.github.com/actions)
- [Android编译指南](https://github.com/actions/setup-android)

### 社区支持
- [GitHub社区论坛](https://github.community/)
- [Stack Overflow](https://stackoverflow.com/questions/tagged/github-actions+android)

### 问题反馈
如果在部署过程中遇到问题，请：
1. 查看GitHub Actions日志
2. 检查错误信息
3. 在GitHub仓库创建Issue

---

通过以上步骤，你可以轻松地在GitHub上自动编译APK，并随时下载最新版本的应用。这个方案完全免费，适合个人和小型项目使用。