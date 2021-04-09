const axios = require('axios')

module.exports = config => {
  // 创建 axios 实例
  const instance = axios.create({
    timeout: 15000,
    headers: {
      Connection: 'keep-alive',
      'Upgrade-Insecure-Requests': 1,
      DNT: 1,
      'User-Agent':
        'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36 Edg/89.0.774.68',
      Accept:
        'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
      'Sec-Fetch-Site': 'same-origin',
      'Sec-Fetch-Mode': 'navigate',
      'Sec-Fetch-User': '?1',
      'Sec-Fetch-Dest': 'document',
      'Accept-Encoding': 'gzip, deflate, br',
      'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6',
      Cookie: 'JSESSIONID=FBC1ACBAAB5F9D259B7F8B2CEE72BDF9',
      'sec-gpc': 1,
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
          status: 502,
          statusText: 'Bad Gateway',
        }
      }
      return error.response.status
    }
  )

  // 3. 发送网络请求
  return instance(config)
}
