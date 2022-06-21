// 使用node.js的express框架部署vue项目，请保证先执行npm run build打包项目，然后执行node.js运行项目

// 导入 express 模块
const express = require('express')
// 解决vue的host模式下History模型下刷新404的问题
const history = require('connect-history-api-fallback')
// 创建 express 的服务器实例
const app = express()
// 托管静态资源
app.use(history())
app.use(express.static('./dist'))
// 调用 app.listen 方法，指定端口号并启动 web 服务器
const port = 8010
app.listen(port, function () {
  console.log('Express server running at http://127.0.0.1:' + port)
})
