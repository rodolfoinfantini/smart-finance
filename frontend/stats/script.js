import { request } from '../modules/request.js'

document.querySelector('.back').onclick = () => {
    location.href = '/'
}

const balance = await request('/balance')

document.querySelector('h1').innerText = `Estatísticas de ${localStorage.getItem('name')}`

const formatter = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
})

const stats = {
    balance: document.querySelector('.stats .balance'),
    spent: document.querySelector('.stats .spent'),
    income: document.querySelector('.stats .income'),
}
for (const stat in stats) {
    const total = stats[stat].querySelector('.stat.total .value')
    const max = stats[stat].querySelector('.stat.max .value')
    const min = stats[stat].querySelector('.stat.min .value')
    total.innerText = formatter.format(balance.body[`total${firstLetterUpperCase(stat)}`])
    max.innerText = formatter.format(balance.body[`max${firstLetterUpperCase(stat)}`])
    min.innerText = formatter.format(balance.body[`min${firstLetterUpperCase(stat)}`])

    if (stat === 'balance') {
        const totalValue = balance.body[`total${firstLetterUpperCase(stat)}`]
        if (totalValue < 0) {
            total.classList.add('negative')
        } else if (totalValue > 0) {
            total.classList.add('positive')
        }

        const maxValue = balance.body[`max${firstLetterUpperCase(stat)}`]
        if (maxValue < 0) {
            max.classList.add('negative')
        } else if (maxValue > 0) {
            max.classList.add('positive')
        }

        const minValue = balance.body[`min${firstLetterUpperCase(stat)}`]
        if (minValue < 0) {
            min.classList.add('negative')
        } else if (minValue > 0) {
            min.classList.add('positive')
        }
    }
}

function firstLetterUpperCase(text) {
    return text.charAt(0).toUpperCase() + text.slice(1)
}

document.querySelector('.loading').remove()
