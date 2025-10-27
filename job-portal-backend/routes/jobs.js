const mongoose = require('mongoose');

const JobSchema = new mongoose.Schema({
    // Fields matching the structure expected by the frontend
    title: {
        type: String,
        required: true,
        trim: true
    },
    company: {
        type: String,
        required: true,
        trim: true
    },
    location: {
        type: String,
        required: true,
        trim: true
    },
    jobType: {
        type: String,
        enum: ['Full-Time', 'Part-Time', 'Contract', 'Internship'],
        default: 'Full-Time'
    },
    salaryRange: {
        type: String,
        required: false
    },
    description: {
        type: String,
        required: true
    },
    descriptionSnippet: { // Used by the frontend list view
        type: String,
        required: false
    },
    recruiterId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    postDate: {
        type: Date,
        default: Date.now
    }
});

// Helper to ensure the ID returned matches the 'id' field in Kotlin
JobSchema.virtual('id').get(function() {
    return this._id.toHexString();
});
JobSchema.set('toJSON', { virtuals: true });

module.exports = mongoose.model('Job', JobSchema);