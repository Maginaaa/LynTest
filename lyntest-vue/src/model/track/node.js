import { post, get } from '@/lin/plugin/axios'

class Node {
  async createNode(node) {
    const res = await post('/track/node/create', node)
    return res
  }

  async getNodeByProjectId(id) {
    const res = await get(`/track/node/${id}`)
    return res
  }

  async updateNode(node) {
    const res = await post('/track/node/update', node)
    return res
  }

  async deleteNodes(ids) {
    const res = await post('/track/node/delete', ids)
    return res
  }

  async dragNode(param) {
    const res = await post('/track/node/drag', param)
    return res
  }

  async nodePosition(ids) {
    const res = await post('/track/node/position', ids)
    return res
  }
}

export default new Node()
