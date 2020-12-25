import { post, get } from '@/lin/plugin/axios'

class HttpCase {
  async searchCaseList(data) {
    const res = await post('/autotest/http/list', data)
    return res
  }

  async createCase(data) {
    const res = await post('/autotest/http/create', data)
    return res
  }

  async updateCase(data) {
    const res = await post('/autotest/http/update', data)
    return res
  }

  async deleteCase(ids) {
    const res = await post('/autotest/http/delete', ids)
    return res
  }

  async execute(data) {
    const res = await post('/autotest/http/execute', data)
    return res
  }

  async getCreatorList() {
    const res = await get('/autotest/http/creator')
    return res
  }
}

export default new HttpCase()
