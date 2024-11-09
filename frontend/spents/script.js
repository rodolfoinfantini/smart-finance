import { request } from '../modules/request.js'

document.querySelector('.back').onclick = () => {
    location.href = '/'
}

const categories = await request('/spents/categories')

const categoryWrapper = document.querySelector('.autocomplete-wrapper.category')
const categoryInput = categoryWrapper.querySelector('input')
let search = ''
let selected = 0
for (const category of categories.body) {
    const categoryElement = document.createElement('button')
    categoryElement.innerText = category
    categoryElement.onclick = () => {
        categoryInput.value = category
    }
    categoryWrapper.querySelector('.autocomplete').appendChild(categoryElement)
}

function selectCategory() {
    const selectedCategory = categoryWrapper.querySelectorAll('button.selected')
    for (const category of selectedCategory) {
        category.classList.remove('selected')
    }

    const category = categoryWrapper.querySelectorAll('button:not(.hidden)')
    if (category[selected]) {
        category[selected].classList.add('selected')
    }
}

categoryInput.onkeydown = (event) => {
    if (categories.body.length === 0) return
    if (event.key === 'Enter') {
        event.preventDefault()
        const category = categoryWrapper.querySelector('button.selected')
        if (category) {
            category.click()
        }
        return
    }
    if (event.key === 'ArrowDown') {
        event.preventDefault()
        const categoryCount = categoryWrapper.querySelectorAll('button:not(.hidden)').length
        selected++
        if (selected >= categoryCount) {
            selected = 0
        }
        selectCategory()
    } else if (event.key === 'ArrowUp') {
        event.preventDefault()
        const categoryCount = categoryWrapper.querySelectorAll('button:not(.hidden)').length
        selected--
        if (selected < 0) {
            selected = categoryCount - 1
        }
        selectCategory()
    }
}

categoryInput.oninput = () => {
    if (categories.body.length === 0) return
    search = categoryInput.value.toLowerCase()
    for (const category of categoryWrapper.querySelectorAll('button')) {
        if (category.innerText.toLowerCase().includes(search)) {
            category.classList.remove('hidden')
        } else {
            category.classList.add('hidden')
        }
    }

    selected = 0
    selectCategory()
}

categoryInput.onblur = () => {
    if (categories.body.length === 0) return
    setTimeout(() => {
        categoryWrapper.querySelector('.autocomplete').classList.add('hidden')
        search = ''
        selected = 0
        for (const category of categoryWrapper.querySelectorAll('button'))
            category.classList.remove('hidden')
        document.querySelector('.backdrop').classList.add('hidden')
    }, 100)
}
categoryInput.onfocus = () => {
    if (categories.body.length === 0) return
    selectCategory()
    categoryWrapper.querySelector('.autocomplete').classList.remove('hidden')
    document.querySelector('.backdrop').classList.remove('hidden')
}

const form = document.querySelector('form')

form.onsubmit = async (event) => {
    event.preventDefault()

    const category = form.category.value
    const value = form.value.value

    if (!category || !value) {
        return
    }

    const response = await request('/spents', 'POST', { category, value })
    if (response.status === 200) {
        location.href = '/'
    } else {
        document.querySelector('.error').innerText = response.body.message
    }
}

document.querySelector('.loading').remove()
