import { Router } from 'express'
import { validateToken } from './middlewares/tokenMiddleware.js'
import { Income, toDto } from '../data/entities/income.js'

const incomesRouter = Router()

incomesRouter.use(validateToken)

incomesRouter.get('/', async (req, res) => {
    const incomes = await Income.findAll({
        where: {
            userId: req.token.payload.userId,
        },
        order: [['createdAt', 'DESC']],
    })
    res.json(incomes.map(toDto))
})

incomesRouter.post('/', async (req, res) => {
    const { value, description } = req.body

    if (!value || value <= 0) return res.status(400).json({ message: 'Invalid value' })

    const createdIncome = await Income.create({
        userId: req.token.payload.userId,
        value,
        description: description.trim(),
    })
    res.json(toDto(createdIncome))
})

export default incomesRouter
