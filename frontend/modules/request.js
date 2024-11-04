export const backend = 'http://localhost:3000'

export function sendToLogin() {
    localStorage.removeItem('token')
    localStorage.removeItem('name')

    window.location.href = '/login'
}

export async function request(url, method = 'GET', body = null) {
    const token = localStorage.getItem('token')
    if (!token) {
        sendToLogin()
        throw new Error('No token')
    }

    const headers = {
        Authorization: `Bearer ${token}`,
    }
    if (body != null) {
        headers['Content-Type'] = 'application/json'
    }

    const fetchOptions = {
        method,
        headers,
    }
    if (body) {
        fetchOptions.body = JSON.stringify(body)
    }
    const response = await fetch(`${backend}${url}`, fetchOptions)
    if (response.status === 401) {
        sendToLogin()
        throw new Error('Unauthorized')
    }

    return {
        body: await response.json(),
        status: response.status,
    }
}
