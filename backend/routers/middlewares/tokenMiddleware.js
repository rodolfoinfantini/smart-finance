import jwt from 'jsonwebtoken'

export function validateToken(req, res, next) {
    let token = req.headers['authorization']
    if (!token) return res.status(401).json({ message: 'Token is required' })

    token = token.replace('Bearer ', '').trim()
    if (!token) return res.status(401).json({ message: 'Token is required' })

    jwt.verify(token, process.env.JWT_SECRET, (err, decoded) => {
        if (err) return res.status(401).json({ message: 'Invalid token' })

        req.token = {
            raw: token,
            payload: decoded,
        }
        next()
    })
}
