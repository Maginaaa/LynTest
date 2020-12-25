import { post, _delete } from '@/lin/plugin/axios'

class Variable {
  async getVariableList(data) {
    const res = await post('/autotest/variable/list', data)
    return res
  }

  async createVariable(data) {
    const res = await post('/autotest/variable/create', data)
    return res
  }

  async updateVariable(data) {
    const res = await post('/autotest/variable/update', data)
    return res
  }

  async deleteVariable(id) {
    const res = await _delete(`/autotest/variable/${id}`)
    return res
  }
}

export default new Variable()
