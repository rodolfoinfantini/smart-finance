import { Router } from 'express'
import { validateToken } from './middlewares/tokenMiddleware.js'
import { Spent, toDto } from '../data/entities/spent.js'
import { Category } from '../data/entities/category.js'

const spentsRouter = Router()

spentsRouter.use(validateToken)

spentsRouter.get('/categories', async (req, res) => {
    const categories = await Category.findAll({
        where: {
            userId: req.token.payload.userId,
        },
    })
    res.json(categories.map((category) => category.name))
})

spentsRouter.get('/', async (req, res) => {
    const spents = await Spent.findAll({
        where: {
            userId: req.token.payload.userId,
        },
        order: [['createdAt', 'DESC']],
    })

    const categories = {}
    res.json(
        await Promise.all(
            spents.map(async (spent) => {
                const category =
                    categories[spent.categoryId] ?? (await Category.findByPk(spent.categoryId))
                categories[spent.categoryId] = category
                return toDto(spent, category)
            }),
        ),
    )
})

spentsRouter.post('/', async (req, res) => {
    const { value, category } = req.body

    if (!value || value <= 0) return res.status(400).json({ message: 'Invalid value' })
    if (!category || !category.trim())
        return res.status(400).json({ message: 'Category is required' })

    let savedCategory = await Category.findOne({
        where: {
            userId: req.token.payload.userId,
            name: category,
        },
    })
    if (!savedCategory) {
        savedCategory = await Category.create({
            userId: req.token.payload.userId,
            name: category,
        })
    }

    const spent = await Spent.create({
        userId: req.token.payload.userId,
        value,
        categoryId: savedCategory.id,
    })
    res.json(toDto(spent, savedCategory))
})

export default spentsRouter
