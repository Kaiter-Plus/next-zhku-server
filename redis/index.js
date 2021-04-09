var redis = require('redis'),
  RDS_PORT = 6379, // 端口号
  RDS_HOST = '121.37.230.214', // 服务器IP  要连接的服务器redis
  RDS_PWD = 'zheng1234', // 密码
  RDS_OPTS = {}, // 设置项
  client = redis.createClient(RDS_PORT, RDS_HOST, RDS_OPTS)

// 认证链接
client.auth(RDS_PWD)
// 认证链接
// client.auth(RDS_PWD, function () {
//   console.log('通过认证')
// })

// client.on('connect', function () {
//   console.log('connect...')
// })

// client.on('ready', function (err) {
//   console.log('ready')
// })

/* 
    添加string类型的数据
  	@param  _key 键
  	@params value 值
  	@params expire (过期时间,单位秒;为空表示不过期)
  	@param  callBack(err,result)
*/
const redisStorage = (function () {
  var mod = {}
  // 设置
  mod.set = function (_key, value, expire, callback) {
    client.set(_key, JSON.stringify(value), function (err, result) {
      if (err) {
        // console.log(err)
        if (typeof callback === 'function') {
          callback(err, null)
        }
        return
      }
      if (!isNaN(expire) && expire > 0) {
        client.expire(_key, parseInt(expire))
      }
      if (typeof callback === 'function') {
        callback(result)
      }
    })
  }
  // 获取
  mod.get = function (_key, callback) {
    client.get(_key, function (err, result) {
      if (err) {
        // console.log(err)
        if (typeof callback === 'function') {
          callback(err, null)
        }
        return
      }
      if (typeof callback === 'function') {
        callback(JSON.parse(result))
      }
    })
  }
  // 删除
  mod.del = function (_key, callback) {
    client.del(_key, function (err, result) {
      if (err) {
        // console.log(err)
        if (typeof callback === 'function') {
          callback(err, null)
        }
        return
      }
      if (typeof callback === 'function') {
        // 删除返回 1 或 0
        callback(result)
      }
    })
  }
  return mod
})()

module.exports = redisStorage

// redisStorage.set('测试', '666', 1, function (result) {
//   console.log(result) //存入的状态,正常则为 "OK"
// })

// redisStorage.get('测试', function (result) {
//   console.log(result) //取出的值
// })
