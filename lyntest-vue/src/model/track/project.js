import { post, get, _delete } from '@/lin/plugin/axios'

class Project {
  async getAllProkect() {
    const res = await get('/track/project/all')
    return res
  }

  async searchProjectList(data) {
    const res = await post('/track/project/list', data)
    return res
  }

  async getNewProject(count) {
    const data = {
      name: '',
      page: 1,
      count,
    }
    const res = await post('/track/project/list', data)
    return res
  }

  async createProject(data) {
    const res = await post('/track/project/create', data)
    return res
  }

  async updateProject(data) {
    const res = await post('/track/project/update', data)
    return res
  }

  async deleteProject(id) {
    const res = await _delete(`/track/project/${id}`)
    return res
  }
}

export default new Project()
