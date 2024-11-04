import { Router } from 'express'
import { hash, compare } from 'bcrypt'
import jwt from 'jsonwebtoken'
import { User, toDto } from '../data/entities/user.js'
import { validateToken } from './middlewares/tokenMiddleware.js'

const usersRouter = Router()

function createToken(user) {
    return jwt.sign({ userId: user.id, email: user.email }, process.env.JWT_SECRET, {
        expiresIn: '12h',
        audience: 'user',
        issuer: 'smart-finance',
    })
}

usersRouter.get('/self', validateToken, async (req, res) => {
    const user = await User.findByPk(req.token.payload.userId)
    res.json(toDto(user))
})

usersRouter.post('/', async (req, res) => {
    const { email, firstName, lastName, password } = req.body

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!email || !email.trim() || !emailRegex.test(email))
        return res.status(400).json({ message: 'Invalid e-mail' })
    if (!firstName || !firstName.trim())
        return res.status(400).json({ message: 'First name is required' })
    if (!password || password.length < 6)
        return res.status(400).json({ message: 'Password must be at least 6 characters long' })

    const alreadyCreatedUser = await User.findOne({
        where: {
            email,
        },
    })
    if (alreadyCreatedUser)
        return res.status(400).json({ message: `User already exists with e-mail: ${email}` })

    const passwordHash = await hash(password, 10)

    const user = await User.create({
        email: email.trim(),
        firstName: firstName.trim(),
        lastName: lastName?.trim(),
        password: passwordHash,
    })

    res.json({ ...toDto(user), token: createToken(user) })
})

usersRouter.post('/login', async (req, res) => {
    const { email, password } = req.body

    const user = await User.findOne({
        where: {
            email,
        },
    })
    if (!user) return res.status(401).json({ message: 'Invalid email or password' })

    const passwordMatch = await compare(password, user.password)
    if (!passwordMatch) return res.status(400).json({ message: 'Invalid email or password' })

    res.json({ token: createToken(user) })
})

export default usersRouter
