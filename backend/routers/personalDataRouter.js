import { Router } from 'express'
import { validateToken } from './middlewares/tokenMiddleware.js'
import { PersonalData, toDto } from '../data/entities/personalData.js'

const personalDataRouter = Router()

personalDataRouter.use(validateToken)

personalDataRouter.get('/self', async (req, res) => {
    const personalData = await PersonalData.findOne({
        where: {
            userId: req.token.payload.userId,
        },
    })
    if (!personalData) return res.status(404).json({ message: 'Personal data not found' })

    res.json(toDto(personalData))
})

personalDataRouter.post('/', async (req, res) => {
    const personalData = await PersonalData.findOne({
        where: {
            userId: req.token.payload.userId,
        },
    })
    if (personalData) return res.status(400).json({ message: 'Your personal data already exists' })

    const { fullName, age, occupation, mensalIncome } = req.body

    if (!fullName || !fullName.trim())
        return res.status(400).json({ message: 'Full name is required' })
    if (!age || age < 8 || age > 120) return res.status(400).json({ message: 'Invalid age' })
    if (!occupation || !occupation.trim())
        return res.status(400).json({ message: 'Occupation is required' })
    if (!mensalIncome || mensalIncome < 0)
        return res.status(400).json({ message: 'Invalid mensal income' })

    const createdPersonalData = await PersonalData.create({
        userId: req.token.payload.userId,
        fullName: fullName.trim(),
        age,
        occupation: occupation.trim(),
        mensalIncome,
    })
    res.json(toDto(createdPersonalData))
})

export default personalDataRouter
