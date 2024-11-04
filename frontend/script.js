import { validateToken } from './modules/validateToken.js'
import { request, sendToLogin } from './modules/request.js'

const logoutButton = document.querySelector('.logout')
logoutButton.onclick = sendToLogin

await validateToken()

const personalData = await request('/personal-data/self')
if (personalData.status === 404) {
    window.location.href = '/personal-data'
    throw new Error('No personal data found')
}
document.querySelector('.loading').remove()
