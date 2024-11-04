import { request } from './request.js'

export async function validateToken() {
    await request('/users/self')
}
