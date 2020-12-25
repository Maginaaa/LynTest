import { post, get } from '@/lin/plugin/axios'

class Pressure {
  async execute(data) {
    const res = await post('/autotest/pressure/execute', data)
    return res
  }

  async getReportDetail(id) {
    const res = await get(`/autotest/pressure/${id}`)
    return res
  }

  async getReportIdList(id) {
    const res = await get(`/autotest/pressure/list/${id}`)
    return res
  }
}

export default new Pressure()
