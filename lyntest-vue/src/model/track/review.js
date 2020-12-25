import { post, _delete } from '@/lin/plugin/axios'

class Review {
  async searchReviewList(data) {
    const res = await post('/track/review/list', data)
    return res
  }

  async createReview(data) {
    const res = await post('/track/review/create', data)
    return res
  }

  async updateReview(data) {
    const res = await post('/track/review/update', data)
    return res
  }

  async deleteReview(id) {
    const res = await _delete(`/track/review/${id}`)
    return res
  }
}

export default new Review()
