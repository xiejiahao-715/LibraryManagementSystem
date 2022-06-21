// ElementPlus自动导入
const AutoImport = require('unplugin-auto-import/webpack')
const Components = require('unplugin-vue-components/webpack')
const { ElementPlusResolver } = require('unplugin-vue-components/resolvers')
// gzip压缩插件
const CompressionPlugin = require("compression-webpack-plugin")

module.exports = {
  devServer: {
    port: 8000
  },
  configureWebpack: {
    plugins: [
      // ElementPlus的自动导入
      AutoImport({
        resolvers: [ElementPlusResolver()],
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      }),
      new CompressionPlugin({
        test: /\.(js|css|html|svg)$/, // 需要压缩的文件类
        threshold: 1024 * 5, // 归档需要进行压缩的文件大小最小值，我这个是5K以上的进行压缩
        deleteOriginalAssets: false, // 是否删除原文件
      })
    ],
  }
}
