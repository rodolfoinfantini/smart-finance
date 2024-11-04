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
        return res.status(400).json({ message: 'E-mail inválido' })
    if (!firstName || !firstName.trim())
        return res.status(400).json({ message: 'Digite o primeiro nome' })
    if (!password || password.length < 6)
        return res.status(400).json({ message: 'A senha deve ter no mínimo 6 caracteres' })

    const alreadyCreatedUser = await User.findOne({
        where: {
            email,
        },
    })
    if (alreadyCreatedUser)
        return res.status(400).json({ message: `Já existe um usuário com o e-mail ${email}` })

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
    if (!user) return res.status(401).json({ message: 'E-mail ou senha inválidos' })

    const passwordMatch = await compare(password, user.password)
    if (!passwordMatch) return res.status(400).json({ message: 'E-mail ou senha inválidos' })

    res.json({ token: createToken(user), name: user.firstName })
})

export default usersRouter
