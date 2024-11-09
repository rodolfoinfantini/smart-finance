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

const h1 = document.querySelector('h1')
h1.innerText = `Olá, ${localStorage.getItem('name')}!\n${h1.innerText}`

const balance = await request('/balance')

const formatter = new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
})

const stats = {
    balance: document.querySelector('.stat.balance .value'),
    spent: document.querySelector('.stat.spent .value'),
    income: document.querySelector('.stat.income .value'),
}
stats.balance.innerText = formatter.format(balance.body.totalBalance)
if (balance.body.totalBalance < 0) {
    stats.balance.classList.add('negative')
} else if (balance.body.totalBalance > 0) {
    stats.balance.classList.add('positive')
}
stats.spent.innerText = formatter.format(balance.body.totalSpent)
stats.income.innerText = formatter.format(balance.body.totalIncome)

const spents = await request('/spents')
createElements(spents.body, 'spents', balance.body, 'category', 'spent')

const incomes = await request('/incomes')
createElements(incomes.body, 'incomes', balance.body, 'description', 'income')

const btnSpent = document.querySelector('.btn-spent')
const btnIncome = document.querySelector('.btn-income')

btnSpent.onclick = () => {
    btnSpent.classList.add('active')
    btnIncome.classList.remove('active')
    document.querySelector('.tab.spents').classList.add('active')
    document.querySelector('.tab.incomes').classList.remove('active')
}
btnIncome.onclick = () => {
    btnIncome.classList.add('active')
    btnSpent.classList.remove('active')
    document.querySelector('.tab.incomes').classList.add('active')
    document.querySelector('.tab.spents').classList.remove('active')
}

function createElements(data, type, balance, descriptionKey, balanceKey) {
    const container = document.querySelector(`.tab.${type}`)
    container.innerHTML = ''
    const months = []

    const addAnchor = document.createElement('a')
    addAnchor.href = `/${type}`
    addAnchor.innerText = 'Adicionar'
    addAnchor.classList.add('add')
    container.appendChild(addAnchor)

    for (const item of data) {
        const date = new Date(item.createdAt)
        const month = date.getMonth() + 1
        const monthName = firstUpperCase(date.toLocaleString('pt-BR', { month: 'long' }))
        const year = date.getFullYear()
        let monthContainer = months.find((m) => m.month === month && m.year === year)
        if (!monthContainer) {
            monthContainer = {
                monthName,
                month,
                year,
                items: [],
                container: document.createElement('div'),
            }
            months.push(monthContainer)
            monthContainer.container.classList.add('month')
        }

        const itemContainer = document.createElement('div')
        itemContainer.classList.add('item')

        const leftContainer = document.createElement('div')
        leftContainer.classList.add('left')
        const rightContainer = document.createElement('div')
        rightContainer.classList.add('right')

        const descriptionSpan = document.createElement('span')
        descriptionSpan.innerText = item[descriptionKey]
        const timeSpan = document.createElement('span')
        timeSpan.innerText = date.toLocaleString('pt-BR', { hour: '2-digit', minute: '2-digit' })

        leftContainer.appendChild(descriptionSpan)
        leftContainer.appendChild(timeSpan)

        const valueSpan = document.createElement('span')
        valueSpan.innerText = formatter.format(item.value)
        rightContainer.appendChild(valueSpan)

        itemContainer.appendChild(leftContainer)
        itemContainer.appendChild(rightContainer)

        item.container = itemContainer

        monthContainer.items.push(item)
    }

    for (const month of months) {
        const header = document.createElement('div')
        header.classList.add('header')

        const h2 = document.createElement('h2')
        h2.innerText = `${month.monthName} ${month.year}`
        header.appendChild(h2)

        const balanceContainer = document.createElement('div')
        balanceContainer.classList.add('balance')

        const monthBalance = balance.years
            .find((y) => y.year === month.year)
            .months.find((m) => m.month === month.month)

        const totalSpan = document.createElement('span')
        totalSpan.innerText = `Total: ${formatter.format(monthBalance[balanceKey])}`
        balanceContainer.appendChild(totalSpan)
        const balanceSpan = document.createElement('span')
        balanceSpan.innerText = `Saldo: ${formatter.format(monthBalance.balance)}`
        if (monthBalance.balance < 0) {
            balanceSpan.classList.add('negative')
        } else if (monthBalance.balance > 0) {
            balanceSpan.classList.add('positive')
        }
        balanceContainer.appendChild(balanceSpan)
        header.appendChild(balanceContainer)

        month.container.appendChild(header)

        const itemsContainer = document.createElement('div')
        for (const item of month.items) {
            itemsContainer.appendChild(item.container)
        }
        month.container.appendChild(itemsContainer)

        container.appendChild(month.container)
    }
}

function firstUpperCase(text) {
    return text[0].toUpperCase() + text.slice(1)
}

document.querySelector('.loading').remove()
