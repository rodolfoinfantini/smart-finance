import { Router } from 'express'
import { validateToken } from './middlewares/tokenMiddleware.js'
import { Spent } from '../data/entities/spent.js'
import Server from '../server/server.js'
import { Income } from '../data/entities/income.js'

const balanceRouter = Router()

balanceRouter.use(validateToken)

balanceRouter.get('/', async (req, res) => {
    const spents = await Spent.findAll({
        where: {
            userId: req.token.payload.userId,
        },
    })
    const incomes = await Income.findAll({
        where: {
            userId: req.token.payload.userId,
        },
    })
    const balanceMessage = {
        userId: req.token.payload.userId,
        spents: spents.map((s) => {
            return {
                value: s.value,
                createdAt: s.createdAt.toISOString().split('T')[0],
            }
        }),
        incomes: incomes.map((i) => {
            return {
                value: i.value,
                createdAt: i.createdAt.toISOString().split('T')[0],
            }
        }),
    }

    const result = await (await Server().newConnection()).sendEvent('balance', balanceMessage)
    if (result.userId !== req.token.payload.userId)
        return res.status(500).json({ message: 'Something went wrong' })

    res.json(result)
})

export default balanceRouter
