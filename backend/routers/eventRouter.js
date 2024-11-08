import { Router } from 'express'
import Server from '../server/server.js'

const eventRouter = Router()

eventRouter.post('/:event', async (req, res) => {
    const server = await Server()
    const event = req.params.event
    const data = await server.sendEvent(event, req.body)
    res.json(data)
})

export default eventRouter
