import { post, get } from '@/lin/plugin/axios'

class CollectionReport {
  async searchReportList(data) {
    const res = await post('/autotest/collection/report/list', data)
    return res
  }

  async getReportDetail(id) {
    const res = await get(`/autotest/collection/report/${id}`)
    return res
  }

  async execute(id) {
    const res = await post(`/autotest/collection/execute/${id}`)
    return res
  }
}

export default new CollectionReport()
