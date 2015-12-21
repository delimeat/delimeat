var gulp = require('gulp')
    , usemin = require('gulp-usemin')
    , uglify = require('gulp-uglify')
	, del = require('del')
    , minifyHtml = require('gulp-minify-html')
    , minifyCss = require('gulp-minify-css')
    , header = require('gulp-header')
    , inject = require('gulp-inject')
    , templateCache = require('gulp-angular-templatecache')
    , ngAnnotate = require('gulp-ng-annotate')
    , refresh = require('gulp-livereload')
    , jshint = require('gulp-jshint')
    , rev = require('gulp-rev')
	, ngConstant = require('gulp-ng-constant')
	, rename = require("gulp-rename")
    , lrserver = require('tiny-lr')()
    , express = require('express')
    , livereload = require('connect-livereload');

// Header configuration
var pkg = require('./package.json');
var banner = ['/**',
  ' * <%= pkg.name %> - <%= pkg.description %>',
  ' * @version v<%= pkg.version %>',
  ' * @link <%= pkg.homepage %>',
  ' * @license <%= pkg.license %>',
  ' */',
  ''].join('\n');

// Compilation tasks
gulp.task('clean', function (cb) {
  del(['./build','./.tmp'], cb);
});

gulp.task('lint', function() {
    return gulp.src('./src/main/sourceapp/angular/**/*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'))
        .pipe(jshint.reporter('fail'));
});

gulp.task('views', function() {
    return gulp.src('./src/main/sourceapp/angular/**/*.tmpl.html')
        .pipe(templateCache({
            module: 'app',
            root: 'app'
        }))
        .pipe(gulp.dest('./.tmp/js'));
});

gulp.task('compile', ['clean', 'views'], function() {
    var projectHeader = header(banner, { pkg : pkg } );

    gulp.src('./src/main/sourceapp/index.html')
        .pipe(inject(gulp.src('./.tmp/js/templates.js', {read: false}),
            {
                starttag: '<!-- inject:templates:js -->'
                ,ignorePath: '/.tmp'
            }
        ))
        .pipe(usemin({
            css:          [minifyCss(),'concat'],
            //html:         [minifyHtml({ empty: true })],
            js:           [ngAnnotate(), projectHeader],
            js_libs:      []
        }))
        .pipe(gulp.dest('./src/main/webapp/'));
});

gulp.task('default', ['compile']);