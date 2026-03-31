# 词语听写小助手

一个专为小朋友设计的 Android 应用，通过拍照识别词语并自动朗读，帮助家长和老师进行词语听写练习。

## 功能特点

### 🎯 核心功能
- **图片识别**: 支持拍照或从相册选择包含词语的图片
- **文字识别**: 使用 Google ML Kit 进行中文文字识别（完全离线）
- **语音朗读**: 使用 Android 系统 TTS 朗读词语
- **间隔控制**: 可调节朗读间隔时间（5-120秒）
- **播放控制**: 支持开始、暂停、继续、停止

### 🚀 技术优势
- **完全免费**: 所有功能免费使用，无任何费用
- **完全离线**: OCR 识别在设备端完成，无需网络
- **隐私安全**: 不上传任何图片或文字数据
- **儿童友好**: 界面简洁，操作简单，适合儿童使用

## 技术架构

### 技术栈
- **开发语言**: Kotlin
- **最小 SDK**: Android 7.0 (API 24)
- **目标 SDK**: Android 14 (API 34)
- **架构模式**: MVC + 组件化

### 核心组件
1. **OCR 识别**: Google ML Kit Text Recognition Chinese
2. **语音合成**: Android TextToSpeech
3. **相机功能**: CameraX
4. **图片处理**: Glide
5. **UI 框架**: Material Components

## 项目结构

```
WordDictationApp/
├── app/src/main/
│   ├── java/com/example/worddictation/
│   │   ├── MainActivity.kt          # 主界面
│   │   ├── CameraActivity.kt        # 相机界面
│   │   ├── WordsAdapter.kt          # 词语列表适配器
│   │   └── ImagePickerActivity.kt   # 图片选择器
│   ├── res/
│   │   ├── layout/                  # 布局文件
│   │   ├── values/                  # 资源文件
│   │   └── menu/                    # 菜单文件
│   └── AndroidManifest.xml          # 应用清单
├── 编译说明.md                      # 详细编译指南
├── 使用说明.md                      # 用户使用手册
├── compile.sh                       # Linux/macOS 编译脚本
└── compile.bat                      # Windows 编译脚本
```

## 快速开始

### 环境要求
- Android Studio Arctic Fox 或更高版本
- JDK 11 或更高版本
- Android SDK API 24+

### 编译步骤

#### 方法一：使用 Android Studio
1. 使用 Android Studio 打开项目
2. 等待 Gradle 同步完成
3. 点击 Run → Run 'app'

#### 方法二：使用命令行
```bash
# Linux/macOS
chmod +x compile.sh
./compile.sh

# Windows
compile.bat
```

### 安装测试
1. 连接 Android 设备或启动模拟器
2. 启用 USB 调试
3. 运行安装命令：
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 使用指南

### 基本流程
1. **选择图片**: 拍照或从相册选择包含词语的图片
2. **识别词语**: 应用自动识别图片中的中文词语
3. **设置间隔**: 调节朗读间隔时间（默认30秒）
4. **开始朗读**: 点击开始按钮，应用按间隔朗读词语
5. **控制播放**: 可随时暂停、继续或停止

### 界面说明
- **拍照按钮**: 打开相机拍摄新照片
- **相册按钮**: 从相册选择已有图片
- **词语列表**: 显示识别到的词语
- **当前词语**: 高亮显示正在朗读的词语
- **间隔滑块**: 调节朗读间隔时间
- **控制按钮**: 开始、暂停/继续、停止

## 技术实现细节

### OCR 识别流程
1. 使用 CameraX 拍摄图片或从相册选择
2. 将图片转换为 InputImage 对象
3. 使用 Google ML Kit Chinese Text Recognizer 进行识别
4. 提取文本块并分割为词语
5. 过滤非中文字符，生成词语列表

### TTS 语音合成
1. 初始化 TextToSpeech 引擎
2. 设置中文语言环境
3. 配置语音参数（语速、音调）
4. 使用 UtteranceProgressListener 监听朗读进度
5. 按间隔顺序朗读词语

### 定时器机制
1. 使用 Handler + Runnable 实现定时器
2. 支持动态调整间隔时间
3. 暂停时准确记录剩余时间
4. 停止时清理所有定时任务

## 权限说明

### 必要权限
- `android.permission.CAMERA`: 用于拍照功能
- `android.permission.READ_EXTERNAL_STORAGE`: 用于读取相册图片
- `android.permission.WRITE_EXTERNAL_STORAGE`: 用于保存拍摄的照片（仅 Android 10 及以下）

### 权限处理
- 运行时权限请求
- 权限拒绝时的友好提示
- 权限被拒绝时的降级处理

## 兼容性

### 支持设备
- **Android 版本**: 7.0 (API 24) 及以上
- **屏幕尺寸**: 支持各种屏幕尺寸和密度
- **设备类型**: 手机、平板

### 已知限制
- 需要设备支持中文 TTS
- 需要后置摄像头（用于拍照）
- 需要足够的存储空间

## 性能优化

### 内存优化
- 及时释放 Bitmap 资源
- 使用 Glide 进行图片加载和缓存
- 避免内存泄漏

### 电池优化
- 及时停止后台任务
- 合理使用唤醒锁
- 优化定时器机制

### 用户体验优化
- 添加加载提示
- 错误处理完善
- 界面响应优化

## 安全与隐私

### 数据安全
- 所有图片处理和文字识别都在设备本地完成
- 不上传任何用户数据到服务器
- 不收集用户个人信息

### 隐私保护
- 无广告，无跟踪
- 不请求不必要的权限
- 透明化权限使用

## 常见问题

### Q: 识别准确率不高怎么办？
A: 确保图片清晰、光线充足、文字排列整齐。可以尝试调整拍摄角度和距离。

### Q: 语音朗读没有声音？
A: 检查设备音量，确保已下载中文语音包，检查 TTS 设置。

### Q: 应用闪退？
A: 检查设备存储空间，确保 Android 版本符合要求，尝试重启应用。

### Q: 如何调节朗读速度？
A: 目前支持调节朗读间隔时间，朗读速度由系统 TTS 控制。

## 开发计划

### 短期计划
- [ ] 添加词语编辑功能
- [ ] 支持多种朗读模式
- [ ] 添加历史记录功能

### 长期计划
- [ ] 支持多语言识别
- [ ] 添加词语分类功能
- [ ] 支持云端同步

## 贡献指南

欢迎提交 Issue 和 Pull Request！

### 开发规范
1. 使用 Kotlin 进行开发
2. 遵循 Android 开发最佳实践
3. 添加必要的注释和文档
4. 进行充分的测试

### 代码风格
- 使用 4 空格缩进
- 遵循 Kotlin 官方代码风格
- 使用有意义的变量名和函数名

## 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 联系方式

如有问题或建议，请联系：
- **邮箱**: support@example.com
- **GitHub**: [项目地址](https://github.com/your-repo)

## 致谢

感谢以下开源项目：
- [Google ML Kit](https://developers.google.com/ml-kit)
- [CameraX](https://developer.android.com/training/camerax)
- [Material Components](https://material.io/develop/android)

---

**让学习变得更有趣！** 🎉