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

    const name = form.name.value
    const email = form.email.value
    const password = form.password.value
    const confirmPassword = form.confirmpassword.value

    if (password !== confirmPassword) {
        error.innerText = 'Senhas não conferem.'
        sendButton.disabled = false
        return
    }

    const response = await fetch(`${backend}/users`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ email, password, firstName: name }),
    })
    const data = await response.json()

    if (response.status === 400) {
        error.innerText = data.message
        sendButton.disabled = false
        return
    }

    if (!data.token) {
        error.innerText = 'Ocorreu um erro. Tente novamente.'
        sendButton.disabled = false
        return
    }

    console.log(data)

    localStorage.setItem('token', data.token)
    localStorage.setItem('name', data.firstName)

    window.location.href = '/'
}
