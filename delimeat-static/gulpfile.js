var gulp = require('gulp')
	, del = require('del')
    , $ = require('gulp-load-plugins')({
    	  rename: {
    	    'gulp-ng-annotate': 'ngannotate',
    	    'gulp-angular-templatecache':'ngtemplatecache',
    	    'gulp-ng-config':'ngconfig'
    	  }
    	});
 
// Clean output directory
gulp.task('clean', function(){
	return del(['target/tmp', 'target/classes/static'], {dot: true});	
});

// linting
gulp.task('lint', function() {
    return gulp.src('./src/js/**/*.js')
        .pipe($.jshint())
        .pipe($.jshint.reporter('jshint-stylish'))
        .pipe($.jshint.reporter('fail'));
});

//move angular views to js file
gulp.task('views',['clean'], function() {
    return gulp.src('./src/js/**/*.tmpl.html')
        .pipe($.ngtemplatecache({
            module: 'app',
            root: 'js'
        }))
        .pipe(gulp.dest('target/tmp/js'));
});

//copy i18n files
gulp.task('i18n',['clean'],  function () {
	  gulp.src('./src/i18n/*.json')
	  .pipe(gulp.dest('target/classes/static/i18n'));
});

//minimise images
gulp.task('images',['clean'], function() {
    return gulp.src('./src/img/**/*.*')
        //.pipe($.imagemin())
        .pipe(gulp.dest('target/classes/static/img'));
});

//copy bootstrap fonts
gulp.task('glyph-icons',['clean'], function () {
	  gulp.src('./src/components/bootstrap/fonts/*')
	  .pipe(gulp.dest('target/classes/static/fonts'));
});

//copy favion
gulp.task('favicon',['clean'], function () {
	  gulp.src('./src/*.ico')
	  .pipe(gulp.dest('target/classes/static'));
});


//compile the front end
gulp.task('compile',['clean','views','lint','i18n','images','glyph-icons','favicon'], function() {
	return gulp.src('./src/index.html')
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
	.pipe(gulp.dest('target/classes/static'));
});

gulp.task('default',['clean','compile'], function() {});