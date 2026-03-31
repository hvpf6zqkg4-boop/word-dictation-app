    override fun onDestroy() {
        // 停止TTS
        tts.stop()
        tts.shutdown()
        
        // 停止OCR
        ocrRecognizer.close()
        
        // 移除所有回调
        readingRunnable?.let { handler.removeCallbacks(it) }
        
        super.onDestroy()
    }
    
    private fun saveImageToCache(bitmap: Bitmap): Uri? {
        return try {
            val cacheDir = cacheDir
            val imageFile = File.createTempFile("word_dictation_", ".jpg", cacheDir)
            
            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }
            
            Uri.fromFile(imageFile)
        } catch (e: Exception) {
            Log.e(TAG, "保存图片到缓存失败", e)
            null
        }
    }
    
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            Log.e(TAG, "从URI获取Bitmap失败", e)
            null
        }
    }
    
    private fun showErrorDialog(title: String, message: String) {
        runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show()
        }
    }
    
    private fun showSuccessDialog(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    // 辅助函数：检查权限
    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == 
               PackageManager.PERMISSION_GRANTED
    }
    
    // 辅助函数：请求权限
    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
    
    // 辅助函数：显示Toast
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    // 辅助函数：显示长Toast
    private fun showLongToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
    
    // 辅助函数：更新状态文本
    private fun updateStatusText(message: String) {
        runOnUiThread {
            binding.recognitionStatusTextView.text = message
        }
    }
    
    // 辅助函数：显示加载状态
    private fun showLoading(message: String = "处理中...") {
        runOnUiThread {
            binding.recognitionStatusTextView.text = message
            // 可以在这里添加进度条
        }
    }
    
    // 辅助函数：隐藏加载状态
    private fun hideLoading() {
        runOnUiThread {
            // 可以在这里隐藏进度条
        }
    }
    
    // 辅助函数：检查网络连接
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(android.net.ConnectivityManager::class.java)
        return connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.let { capabilities ->
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR)
            }
        } ?: false
    }
    
    // 辅助函数：检查TTS是否就绪
    private fun isTTSReady(): Boolean {
        return tts.isSpeaking
    }
    
    // 辅助函数：检查是否有词语
    private fun hasWords(): Boolean {
        return wordsList.isNotEmpty()
    }
    
    // 辅助函数：获取当前词语
    private fun getCurrentWord(): String {
        return if (currentWordIndex < wordsList.size) {
            wordsList[currentWordIndex]
        } else {
            ""
        }
    }
    
    // 辅助函数：获取总词语数
    private fun getTotalWords(): Int {
        return wordsList.size
    }
    
    // 辅助函数：获取当前进度
    private fun getCurrentProgress(): String {
        return "${currentWordIndex + 1}/${wordsList.size}"
    }
    
    // 辅助函数：重置朗读状态
    private fun resetReadingState() {
        isReading = false
        isPaused = false
        currentWordIndex = 0
        readingRunnable?.let { handler.removeCallbacks(it) }
        readingRunnable = null
        updateControlButtons()
        updateCurrentWord()
    }
    
    // 辅助函数：清理资源
    private fun cleanupResources() {
        // 清理图片缓存
        currentImageUri?.let { uri ->
            try {
                val file = File(uri.path ?: return@let)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                Log.e(TAG, "清理图片缓存失败", e)
            }
        }
        
        // 清理列表
        wordsList.clear()
        wordsAdapter.updateWords(emptyList())
        
        // 重置UI
        runOnUiThread {
            binding.previewImageView.visibility = android.view.View.GONE
            binding.recognitionStatusTextView.text = ""
            binding.currentWordTextView.text = ""
            binding.progressTextView.text = ""
            binding.noWordsTextView.visibility = android.view.View.VISIBLE
            binding.wordsRecyclerView.visibility = android.view.View.GONE
        }
    }
    
    // 辅助函数：显示帮助对话框
    private fun showHelpDialog() {
        AlertDialog.Builder(this)
            .setTitle("使用帮助")
            .setMessage("""
                1. 点击"拍照"按钮拍摄包含词语的图片
                2. 或点击"从相册选择"选择已有图片
                3. 系统会自动识别图片中的中文词语
                4. 调整朗读间隔时间（5-120秒）
                5. 点击"开始"按钮开始朗读
                6. 可以随时暂停、继续或停止
                
                注意：
                - 首次使用需要下载中文语音包
                - 确保图片中的文字清晰可见
                - 建议在安静的环境中使用
            """.trimIndent())
            .setPositiveButton(R.string.ok, null)
            .show()
    }
    
    // 辅助函数：显示关于对话框
    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("关于词语听写小助手")
            .setMessage("""
                版本：1.0
                
                功能：
                - 拍照或选择图片识别词语
                - 自动朗读识别到的词语
                - 可调节朗读间隔时间
                - 支持暂停、继续、停止
                
                技术：
                - 使用Google ML Kit进行OCR识别（完全离线）
                - 使用Android系统TTS进行语音合成
                - 所有功能完全免费
                
                开发者：OpenClaw AI助手
                为小朋友学习设计
            """.trimIndent())
            .setPositiveButton(R.string.ok, null)
            .show()
    }
    
    // 菜单相关（可选）
    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_help -> {
                showHelpDialog()
                true
            }
            R.id.menu_about -> {
                showAboutDialog()
                true
            }
            R.id.menu_clear -> {
                cleanupResources()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}