import { Router } from 'express'
import Server from '../server/server.js'

const pingRouter = Router()

pingRouter.get('/', async (_, res) => {
    const server = await Server()
    const data = await server.sendEvent('ping', { ping: 'ping from nodejs' })
    res.json(data)
})

export default pingRouter
