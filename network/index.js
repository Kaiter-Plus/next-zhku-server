const axios = require('axios')

module.exports = config => {
  // 创建 axios 实例
  const instance = axios.create({
    timeout: 5000,
    headers: {
      'User-Agent':
        'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.36',
    },
  })
  // axios 拦截器
  instance.interceptors.request.use(
    config => {
      return config
    },
    error => {
      return error
    }
  )
  instance.interceptors.response.use(
    res => {
      return res.data
    },
    error => {
      if (!error.response) {
        error.response = {
          status: 404,
        }
      }
      throw error.response.status
    }
  )

  // 3. 发送网络请求
  return instance(config)
}
