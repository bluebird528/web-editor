#!/bin/bash
#
# 커밋 메시지 자동 생성 스크립트
# 변경사항을 분석하여 Conventional Commits 형식의 메시지를 생성합니다.
#

# 변경된 파일 목록 가져오기 (staged files)
STAGED_FILES=$(git diff --cached --name-only)

if [ -z "$STAGED_FILES" ]; then
    echo ""
    exit 0
fi

# 변경 타입 분석
ADDED_FILES=$(git diff --cached --name-only --diff-filter=A)
MODIFIED_FILES=$(git diff --cached --name-only --diff-filter=M)
DELETED_FILES=$(git diff --cached --name-only --diff-filter=D)
RENAMED_FILES=$(git diff --cached --name-only --diff-filter=R)

# scope 결정 함수
determine_scope() {
    local files="$1"

    # Java 파일 패턴 분석
    if echo "$files" | grep -q "controller/"; then
        echo "api"
    elif echo "$files" | grep -q "service/"; then
        echo "service"
    elif echo "$files" | grep -q "repository/"; then
        echo "repository"
    elif echo "$files" | grep -q "entity/"; then
        echo "entity"
    elif echo "$files" | grep -q "dto/"; then
        echo "dto"
    elif echo "$files" | grep -q "security/"; then
        echo "security"
    elif echo "$files" | grep -q "config/"; then
        echo "config"

    # 설정 파일
    elif echo "$files" | grep -q "application.yml\|application.properties"; then
        echo "config"
    elif echo "$files" | grep -q "pom.xml\|build.gradle"; then
        echo "build"
    elif echo "$files" | grep -q "docker-compose.yml\|Dockerfile"; then
        echo "docker"

    # 문서 파일
    elif echo "$files" | grep -q "README\|\.md$"; then
        echo "docs"

    # 테스트 파일
    elif echo "$files" | grep -q "test/"; then
        echo "test"

    # Git 관련
    elif echo "$files" | grep -q "\.git"; then
        echo "git"

    else
        echo ""
    fi
}

# type 결정 함수
determine_type() {
    local added="$1"
    local modified="$2"
    local deleted="$3"
    local files="$4"

    # 문서만 변경
    if echo "$files" | grep -q "\.md$" && ! echo "$files" | grep -qv "\.md$"; then
        echo "docs"
        return
    fi

    # 테스트만 변경
    if echo "$files" | grep -q "test/" && ! echo "$files" | grep -qv "test/"; then
        echo "test"
        return
    fi

    # 설정 파일만 변경
    if echo "$files" | grep -qE "(application\.yml|\.properties|pom\.xml|build\.gradle|\.gitignore|docker-compose\.yml)" && \
       ! echo "$files" | grep -qE "src/main/java/"; then
        echo "chore"
        return
    fi

    # 삭제가 있으면
    if [ -n "$deleted" ]; then
        echo "refactor"
        return
    fi

    # 새 파일 추가가 많으면
    added_count=$(echo "$added" | grep -c ".")
    modified_count=$(echo "$modified" | grep -c ".")

    if [ "$added_count" -gt 0 ] && [ "$modified_count" -eq 0 ]; then
        echo "feat"
        return
    fi

    # 수정이 주된 경우
    if [ "$modified_count" -gt 0 ]; then
        # Bug fix 키워드 확인
        if git diff --cached | grep -iqE "(fix|bug|error|exception|null)"; then
            echo "fix"
            return
        fi
        echo "feat"
        return
    fi

    echo "chore"
}

# subject 생성 함수
generate_subject() {
    local type="$1"
    local scope="$2"
    local files="$3"

    # 파일 종류별 설명
    if echo "$files" | grep -q "docker-compose.yml"; then
        echo "add Docker PostgreSQL configuration"
    elif echo "$files" | grep -q "application.yml"; then
        echo "update database configuration"
    elif echo "$files" | grep -q "build.gradle"; then
        echo "update build configuration"
    elif echo "$files" | grep -q "README"; then
        echo "update documentation"
    elif echo "$files" | grep -q ".gitignore"; then
        echo "update gitignore"
    elif echo "$files" | grep -q "controller/"; then
        echo "update API endpoints"
    elif echo "$files" | grep -q "service/"; then
        echo "update business logic"
    elif echo "$files" | grep -q "repository/"; then
        echo "update data access layer"
    elif echo "$files" | grep -q "entity/"; then
        echo "update domain models"
    elif echo "$files" | grep -q "security/"; then
        echo "update security configuration"
    elif echo "$files" | grep -q "test/"; then
        echo "update tests"
    else
        # 파일 개수에 따라
        file_count=$(echo "$files" | wc -l)
        if [ "$file_count" -eq 1 ]; then
            filename=$(basename "$files")
            echo "update $filename"
        else
            echo "update multiple files"
        fi
    fi
}

# 메인 로직
TYPE=$(determine_type "$ADDED_FILES" "$MODIFIED_FILES" "$DELETED_FILES" "$STAGED_FILES")
SCOPE=$(determine_scope "$STAGED_FILES")
SUBJECT=$(generate_subject "$TYPE" "$SCOPE" "$STAGED_FILES")

# 커밋 메시지 생성
if [ -n "$SCOPE" ]; then
    echo "${TYPE}(${SCOPE}): ${SUBJECT}"
else
    echo "${TYPE}: ${SUBJECT}"
fi
