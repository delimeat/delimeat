var gulp = require('gulp')
	, del = require('del')
	, lrserver = require('tiny-lr')()
    , express = require('express')
    , $ = require('gulp-load-plugins')({
    	  rename: {
    	    'gulp-ng-annotate': 'ngannotate',
    	    'gulp-angular-templatecache':'ngtemplatecache',
    	    'gulp-ng-config':'ngconfig'
    	  }
    	});

var SERVER_PORT = 5000;
 
// Clean output directory
gulp.task('clean', function(){
	return del(['target/tmp', 'target/build'], {dot: true});	
});

// linting
gulp.task('lint', function() {
    return gulp.src('./src/main/sourceapp/js/**/*.js')
        .pipe($.jshint())
        .pipe($.jshint.reporter('jshint-stylish'))
        .pipe($.jshint.reporter('fail'));
});

//move angular views to js file
gulp.task('views',['clean'], function() {
    return gulp.src('./src/main/sourceapp/js/**/*.tmpl.html')
        .pipe($.ngtemplatecache({
            module: 'app',
            root: 'js'
        }))
        .pipe(gulp.dest('target/tmp/js'));
});

//copy i18n files
gulp.task('i18n',['clean'],  function () {
	  gulp.src('./src/main/sourceapp/i18n/*.json')
	  .pipe(gulp.dest('target/build/i18n'));
});

//minimise images
gulp.task('images',['clean'], function() {
    return gulp.src('./src/main/sourceapp/img/**/*.*')
        .pipe($.imagemin())
        .pipe(gulp.dest('target/build/img'));
});

//copy bootstrap fonts
gulp.task('glyph-icons',['clean'], function () {
	  gulp.src('./src/main/sourceapp/components/bootstrap/fonts/*')
	  .pipe(gulp.dest('target/build/fonts'));
});

//copy favion
gulp.task('favicon',['clean'], function () {
	  gulp.src('./src/main/sourceapp/*.ico')
	  .pipe(gulp.dest('target/build'));
});

//create config file for api endpoint - DEVELOPMENT
gulp.task('config:dev',['clean'], function () {
	  gulp.src('./src/main/sourceapp/config.json')
	  .pipe($.ngconfig('delimeat.config', {
		  environment: 'development'
		}))
	    .pipe($.rename('config.js'))
	    .pipe(gulp.dest('target/tmp/js'));
});

//create config file for api endpoint - BUILD
gulp.task('config:build',['clean'], function () {
	  gulp.src('./src/main/sourceapp/config.json')
	  .pipe($.ngconfig('delimeat.config', {
		  environment: 'production'
		}))
	    .pipe($.rename('config.js'))
	    .pipe(gulp.dest('target/tmp/js'));
});

//compile the front end
gulp.task('compile',['clean','views','config:build','lint','i18n','images','glyph-icons','favicon'], function() {
	return gulp.src('./src/main/sourceapp/index.html')
    .pipe($.inject(gulp.src('./target/tmp/js/templates.js', {read: false}),
            {
                starttag: '<!-- inject:templates:js -->',
                ignorePath: '/target/tmp'
            }
        ))
    .pipe($.usemin({
        css:          [$.sourcemaps.init(),$.autoprefixer({browsers: ['last 2 versions'],cascade: false}),$.cssnano(),$.rev(),$.sourcemaps.write('.')],
        html:         [$.htmlmin({collapseWhitespace: true})],
        js:           [$.sourcemaps.init(),$.ngannotate(), $.uglify(), $.rev(),$.sourcemaps.write('.')],
        js_libs:      [$.sourcemaps.init(),$.ngannotate(), $.uglify(), $.rev(),$.sourcemaps.write('.')]
    }))
    .pipe($.size({title: 'html',showFiles:true}))
	.pipe(gulp.dest('target/build'));
});

//Serve tasks
gulp.task('reload:html', function () {
    return gulp.src('./src/main/sourceapp/**/*.html')
        .pipe($.livereload(lrserver));
});

//watch for updated source files
gulp.task('watch',['clean'], function () {
    gulp.watch('./src/main/sourceapp/**/*.html', ['reload:html']);
});

//serve the source files for development
gulp.task('serve:dev', ['clean','watch','config:dev'], function() {
    var server = express();
    server.use(express.static('target/tmp/'));
    server.use(express.static('./src/main/sourceapp/'));
    server.listen(SERVER_PORT);
});

gulp.task('default',['clean','compile'], function() {});