import { post, _delete } from '@/lin/plugin/axios'

class Mock {
  async searchMockList(data) {
    const res = await post('/mock/list/', data)
    return res
  }

  async createMock(data) {
    const res = await post('/mock/create', data)
    return res
  }

  async updateMock(data) {
    const res = await post('/mock/update', data)
    return res
  }

  async deleteMock(id) {
    const res = await _delete(`/mock/${id}`)
    return res
  }
}

export default new Mock()
