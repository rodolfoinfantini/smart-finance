import { request } from '../modules/request.js'

const personalData = await request('/personal-data/self')
if (personalData.status !== 404) {
    window.location.href = '/'
    throw new Error('Already registered')
}

const questions = [
    {
        question: 'Qual seu nome completo?',
        field: 'fullName',
        inputType: 'text',
        input: null,
    },
    {
        question: 'Qual sua idade?',
        field: 'age',
        inputType: 'number',
        input: null,
    },
    {
        question: 'Qual seu cargo atual?',
        field: 'occupation',
        inputType: 'text',
        input: null,
    },
    {
        question: 'Qual sua renda mensal?',
        field: 'mensalIncome',
        inputType: 'number',
        input: null,
    },
]
let nextQuestion = 0
const form = document.querySelector('form')
form.onsubmit = async (event) => {
    event.preventDefault()
}

async function submit() {
    const personalData = {}
    for (const question of questions) {
        question.input.disabled = true
        personalData[question.field] = question.input.value
        if (question.input.type === 'number') {
            personalData[question.field] = +personalData[question.field]
        }
    }

    const response = await request('/personal-data', 'POST', personalData)
    if (response.status === 200) {
        window.location.href = '/'
        return
    }

    for (const question of questions) {
        question.input.disabled = false
    }
    alert('Ocorreu um erro. Tente novamente.')
}

function showNextQuestion() {
    const currentQuestion = questions[nextQuestion]
    if (currentQuestion) {
        currentQuestion.input.parentNode.classList.remove('hidden')
        currentQuestion.input.focus()
    } else {
        submit()
    }
    nextQuestion++
}

for (const question of questions) {
    const div = document.createElement('div')
    div.classList.add('question')
    div.classList.add('hidden')

    const label = document.createElement('label')
    label.innerText = question.question
    label.setAttribute('for', question.field)
    div.appendChild(label)

    const input = document.createElement('input')
    input.type = question.inputType
    input.name = question.field
    input.id = question.field
    question.input = input

    input.onkeydown = (event) => {
        if (event.key === 'Enter' && input.value) {
            showNextQuestion()
        }
    }

    div.appendChild(input)

    form.appendChild(div)
}

setTimeout(() => {
    showNextQuestion()
}, 500)
