var createError = require('http-errors')
var express = require('express')
var path = require('path')
var cookieParser = require('cookie-parser')
var logger = require('morgan')

// 导入首页路由
var homeRouter = require('./routes/home/home.js')
var footerRouter = require('./routes/home/footer.js')
var specialRouter = require('./routes/home/special.js')
var friendLinkRouter = require('./routes/home/friendLink.js')
var schoolProfileRouter = require('./routes/home/schoolProfile.js')
var organizationSetupRouter = require('./routes/home/organizationSetup.js')

// 导入新闻页路由
var NewsRouter = require('./routes/news/newsList.js')

// 创建实例
var app = express()

//跨域设置(所有域名)
app.all('*', function (req, res, next) {
  //其中*表示允许所有域可跨
  res.header('Access-Control-Allow-Origin', '*')
  res.header('Access-Control-Allow-Headers', 'Content-Type')
  res.header('Access-Control-Allow-Methods', '*')
  res.header('Content-Type', 'application/json;charset=utf-8')
  next()
})

// 端口
const port = 80

app.use(logger('dev'))
app.use(express.json())
app.use(express.urlencoded({ extended: false }))
app.use(cookieParser())
// 设置静态文件目录，同时设置缓存事件为 1 小时
app.use(express.static(path.join(__dirname, 'public'), { maxAge: 7200 * 1000 }))

// 首页路由导航
app.use('/home', homeRouter)
app.use('/footer', footerRouter)
app.use('/special', specialRouter)
app.use('/friendLink', friendLinkRouter)
app.use('/schoolProfile', schoolProfileRouter)
app.use('/organizationSetup', organizationSetupRouter)

// 新闻页路由导肮
app.use('/news', NewsRouter)

// 捕获 404 错误并前往 错误处理
app.use(function (req, res, next) {
  next(createError(404))
})

// 错误处理
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message
  res.locals.error = req.app.get('env') === 'development' ? err : {}

  // render the error page
  res.status(err.status || 500)
  res.write('code: ' + err.status)
  res.end()
})

// 监听端口
app.listen(port, () => {
  console.log('服务已启动:\nhttp://localhost:%s', port)
})

module.exports = app
