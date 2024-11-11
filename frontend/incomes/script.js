import { request } from '../modules/request.js'

document.querySelector('.back').onclick = () => {
    location.href = '/'
}

const form = document.querySelector('form')

form.onsubmit = async (event) => {
    event.preventDefault()

    const description = form.description.value
    const value = form.value.value

    if (!description || !value) {
        return
    }

    const response = await request('/incomes', 'POST', { description, value })
    if (response.status === 200) {
        location.href = '/'
    } else {
        document.querySelector('.error').innerText = response.body.message
    }
}

document.querySelector('.loading').remove()
