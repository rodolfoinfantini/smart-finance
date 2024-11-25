import { backend } from '../modules/request.js'

if (localStorage.getItem('token')) {
    window.location.href = '/'
    throw new Error('Already logged in')
}

const error = document.querySelector('.error')

const form = document.querySelector('form')
const sendButton = form.querySelector('button')
form.onsubmit = async (event) => {
    sendButton.disabled = true

    event.preventDefault()

    const email = form.email.value
    const password = form.password.value

    const response = await fetch(`${backend}/users/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password }),
    })
    const data = await response.json()

    if (response.status === 401 || response.status === 400) {
        error.innerText = data.message
        sendButton.disabled = false
        return
    }

    if (!data.token) {
        error.innerText = 'Ocorreu um erro. Tente novamente.'
        sendButton.disabled = false
        return
    }

    localStorage.setItem('token', data.token)
    localStorage.setItem('name', data.name)

    window.location.href = '/'
}
