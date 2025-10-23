const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');

const authRoute = require('./routes/auth');
const jobsRoute = require('./routes/jobs');

const app = express();
const PORT = process.env.PORT || 5000;

// IMPORTANT: Ensure your MongoDB server is running and accessible at this URI
const MONGO_URI = 'mongodb://localhost:27017/jobportal';

mongoose.connect(MONGO_URI).then(() => {
  console.log('MongoDB connected successfully.');
}).catch((err) => {
  // This error will crash the app and cause 500s. CHECK YOUR MONGODB SERVICE!
  console.error('CRITICAL: MongoDB connection error. Is the service running?', err);
});

app.use(cors());
app.use(express.json());

// Routes
// /api/auth routes to authRoute
app.use('/api/auth', authRoute);
// FIX: Corrected the typo from './api/jobs' to '/api/jobs'
app.use('/api/jobs', jobsRoute);

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
