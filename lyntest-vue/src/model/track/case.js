import { post, _delete } from '@/lin/plugin/axios'

class TestCase {
  async createCase(data) {
    const res = await post('/track/case/create', data)
    return res
  }

  async getCaseList(data) {
    const res = await post('/track/case/list', data)
    return res
  }

  async updateCase(data) {
    const res = await post('/track/case/update', data)
    return res
  }

  async deleteCase(id) {
    const res = await _delete(`/track/case/${id}`)
    return res
  }
}

export default new TestCase()
