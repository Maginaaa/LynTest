import { post, get, _delete } from '@/lin/plugin/axios'

class Collection {
  async searchCollectionList(data) {
    const res = await post('/autotest/collection/list', data)
    return res
  }

  async getCollectionDetail(id) {
    const res = await get(`/autotest/collection/${id}`)
    return res
  }

  async createCollection(data) {
    const res = await post('/autotest/collection/create', data)
    return res
  }

  async updateCollection(data) {
    const res = await post('/autotest/collection/update', data)
    return res
  }

  async deleteCollection(id) {
    const res = await _delete(`/autotest/collection/${id}`)
    return res
  }

  async getPocketInfo(id) {
    const res = get(`/autotest/collection/pocket/${id}`)
    return res
  }

  async createPocket(data) {
    const res = await post('/autotest/collection/pocket/create', data)
    return res
  }

  async updatePocket(data) {
    const res = await post('/autotest/collection/pocket/update', data)
    return res
  }

  async deletePocket(id) {
    const res = await _delete(`/autotest/collection/pocket/${id}`)
    return res
  }

  async insertCaseToPocket(data) {
    const res = await post('/autotest/collection/case/create', data)
    return res
  }

  async batchInsertCase(data) {
    const res = await post('/autotest/collection/case/batchCreate', data)
    return res
  }

  async deleteCaseFromPocket(data) {
    const res = await post('/autotest/collection/case/delete', data)
    return res
  }

  async batchDeleteCase(data) {
    const res = await post('/autotest/collection/case/batchDelete', data)
    return res
  }

  async nodePosition(data) {
    const res = await post('/autotest/collection/position', data)
    return res
  }
}

export default new Collection()
