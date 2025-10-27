const express = require('express');
const mongoose = require('mongoose');
const authRoutes = require('./routes/auth');
const profileRoutes = require('./routes/profile');
const jobRoutes = require('./routes/jobs')

const app = express();

// --- Configuration ---
const PORT = process.env.PORT || 5000;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/jobportal_db';

// --- Middleware (CRITICAL for logging and body parsing) ---
// This middleware is necessary to read data sent from the Android client
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
// You can add a logging middleware here to confirm all requests hit the server
app.use((req, res, next) => {
    console.log(`[REQUEST]: ${req.method} ${req.originalUrl}`); // <-- Will log every request
    next();
});

// --- Database Connection ---
mongoose.connect(MONGO_URI)
    .then(() => console.log('MongoDB connected successfully.'))
    .catch(err => console.error('MongoDB connection error:', err));

// --- Route Mounting ---
app.use('/api/auth', authRoutes);
app.use('/api/profile', profileRoutes);
app.use('/api/jobs', jobRoutes);

// --- General Status Route ---
app.get('/', (req, res) => {
    res.send('Job Portal Backend is Running!');
});

// --- Server Startup ---
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));