# Contributing to Android Views

Thank you for your interest in contributing to Android Views! This document provides guidelines and instructions for contributing.

## How to Contribute

### Reporting Issues

- Check existing issues to avoid duplicates
- Use a clear and descriptive title
- Provide detailed reproduction steps
- Include Android version, device info, and library version
- Attach screenshots or screen recordings if applicable

### Submitting Changes

1. **Fork the repository** and create your branch from `master`
2. **Make your changes** following the code style guidelines
3. **Test your changes** on multiple API levels if possible
4. **Update documentation** if you change public APIs
5. **Submit a pull request** with a clear description

### Code Style

- Follow standard Kotlin/Java conventions
- Use meaningful variable and function names
- Keep functions focused and small
- Add KDoc/Javadoc for public APIs

### Commit Messages

Use clear, descriptive commit messages:
- `feat: add particle color animation`
- `fix: resolve touch event handling issue`
- `docs: update installation instructions`
- `refactor: simplify particle calculation`

### Pull Request Guidelines

- Keep PRs focused on a single feature or fix
- Include relevant issue numbers
- Add screenshots for UI changes
- Ensure all tests pass
- Update CHANGELOG if applicable

## Development Setup

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run the `app` module for testing

## Adding New Components

When adding a new component:

1. Create a new module following existing naming patterns
2. Follow the existing architecture (attrs.xml, custom View class)
3. Add comprehensive XML attributes for customization
4. Include usage examples in the demo app
5. Update README with documentation

## Questions?

Feel free to open an issue for any questions about contributing.
