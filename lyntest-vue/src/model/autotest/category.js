import { get, post, _delete } from '@/lin/plugin/axios'

class Category {
  async getCategoryList() {
    const res = await get('/autotest/category/list')
    return res
  }

  async createCategory(data) {
    const res = await post('/autotest/category/create', data)
    return res
  }

  async updateCategory(data) {
    const res = await post('/autotest/category/update', data)
    return res
  }

  async deleteCategory(id) {
    const res = await _delete(`/autotest/category/${id}`)
    return res
  }
}

export default new Category()
