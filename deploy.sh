#!/bin/bash

function check_git() {
    print_step "Checking git"
    git --version \
        || print_error "Git is not installed"
}

function check_maven() {
    print_step "Checking Apache Maven"
    mvn --version \
        || print_error "Apache Maven is not installed"
}

function check_directory_is_clean() {
    print_step "Check directory is clean"
    local changed_files_count
    changed_files_count=$(git status --porcelain | wc --lines)
    test "$changed_files_count" -eq 0 \
        || print_error "Working directory is not clean (contains uncommited changes)"
}

function check_branch_is_master() {
    print_step "Check branch is master"
    local current_branch
    current_branch=$(git rev-parse --symbolic-full-name --abbrev-ref HEAD)
    test "$current_branch" = "master" \
        || print_error "Current branch is not master"
}

function ask_confirmation_on_deploy() {
    printf "\nAre you sure you want to perform deploy? (yes/no):\n"
    local answer
    read answer
    test "$answer" = "yes" \
        || print_error "Deploy canceled"
}

function perform_deploy() {
    echo "Starting deployment"

    mvn -B -P release,api-javadoc release:clean release:prepare
    publish_online_javadoc
    mvn -B -P release release:perform
}

function publish_online_javadoc() {
    print_step "Publish online Javadoc"

    cd processor/target/apidocs

    git init
    git remote add javadoc git@github.com:alex-zuy/boilerplate-generator.git
    git fetch --depth=1 javadoc gh-pages
    git add --all
    git commit -m "Update javadoc"
    git merge --no-edit -s ours remotes/javadoc/gh-pages
    git push javadoc master:gh-pages

    cd -
}

function print_step() {
    printf "\n# $1\n"
}

function print_error() {
    echo "Error: " $1
    false
}

check_git \
    && check_maven \
    && check_directory_is_clean \
    && check_branch_is_master \
    && ask_confirmation_on_deploy \
    && perform_deploy
